package com.example.cinema.service;

import com.example.cinema.core.ValidationCustomException;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;
import com.example.cinema.dto.OrderHumanDto;
import com.example.cinema.dto.TimeTableDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Sql({
        "classpath:statements/insert_cinema_hall.sql",
        "classpath:statements/insert_movie.sql",
        "classpath:statements/insert_time_table.sql",
        "classpath:statements/insert_order_table.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:statements/truncate_time_table.sql",
        "classpath:statements/truncate_movie.sql",
        "classpath:statements/truncate_cinema_hall.sql",
        "classpath:statements/truncate_order_table.sql"
})
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private TimeTableService tableService;

    @Test
    void getById() {
        final long id = 137L;
        final Set<Short> seats = Set.of((short) 9, (short) 10, (short) 5, (short) 7, (short) 8);
        final OrderHumanDto b = orderService.getById(id);
        assertEquals(1022L, b.timeTableId());
        assertEquals(id, b.id());
        assertEquals("customer", b.customer());
        assertTrue(b.seats().containsAll(seats));
        assertEquals("Dark of the Moon", b.movieTitle());
        assertEquals("BLACK", b.cinemaHallName());

    }

    @Nested
    @DisplayName("Create order case")
    public class CreateOrder {

        @Test
        void createOrderStartSessionInFuture() {
            final Set<Short> seats = Set.of((short) 1, (short) 2, (short) 4);
            final String customer = "customer";
            final long timeTableId = 1015L;
            final OrderDto order = OrderDto.builder()
                    .customer(customer)
                    .id(null)
                    .seats(seats)
                    .timeTableId(timeTableId)
                    .orderPrice(null)
                    .build();

            final OrderHumanDto dto = assertDoesNotThrow(() -> orderService.createOrder(order));
            assertNotNull(dto.id());
            assertNotNull(dto.orderPrice());
            assertEquals("Transformers", dto.movieTitle());
            assertEquals("BLACK", dto.cinemaHallName());
            assertEquals(LocalDate.now().plusDays(1L).atTime(14, 40, 0), dto.startSession());
            assertTrue(dto.seats().containsAll(seats));
            assertEquals(customer, dto.customer());
            assertEquals(timeTableId, dto.timeTableId());
        }

        @Test
        void createOrderIdNotNull() {
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(-1L)
                    .seats(Set.of((short) 1, (short) 2, (short) 4))
                    .timeTableId(1015L)
                    .orderPrice(null)
                    .build();

            final ValidationCustomException orderSaved = assertThrowsExactly(ValidationCustomException.class, () -> orderService.createOrder(order));
            assertTrue(orderSaved.getMessageMap().containsKey("id"));
            assertTrue(orderSaved.getMessageMap().containsValue("field.error.null"));

        }

        @Test
        void createOrderStartSessionAlreadyStarted() {
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(Set.of((short) 1, (short) 5, (short) 4))
                    .timeTableId(1030L)
                    .orderPrice(null)
                    .build();
            final IllegalArgumentException orderSaved = assertThrowsExactly(IllegalArgumentException.class, () -> orderService.createOrder(order));
            assertEquals("start.session.already.been", orderSaved.getMessage());

        }

        @Test
        void createOrderTimeTableNotfound() {
            final long timeTableId = 10L;
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(Set.of((short) 1, (short) 5, (short) 4))
                    .timeTableId(timeTableId)
                    .orderPrice(null)
                    .build();

            final EntityNotFoundException exc = assertThrowsExactly(EntityNotFoundException.class, () -> orderService.createOrder(order));
            assertEquals(String.format(OrderServiceImpl.TABLE_NOT_FOUND, timeTableId), exc.getMessage());

        }

        @Test
        void createOrderSeatsAlreadyClosed() {
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(Set.of((short) 1, (short) 2, (short) 3))
                    .timeTableId(1015L)
                    .orderPrice(null)
                    .build();

            final IllegalArgumentException exc = assertThrowsExactly(IllegalArgumentException.class, () -> orderService.createOrder(order));
            assertEquals("seats.already.closed", exc.getMessage());

        }

        @Test
        void createOrderSeatsNotRelatedToCinemaHall() {
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(Set.of((short) 1, (short) 5, (short) 11))
                    .timeTableId(1015L)
                    .orderPrice(null)
                    .build();

            final IllegalArgumentException exc = assertThrowsExactly(IllegalArgumentException.class, () -> orderService.createOrder(order));
            assertEquals(TimeTable.BOOKED_SEATS_OUT_OF_RANGE, exc.getMessage());

        }

        @Test
        void createOrderAllSeatsAreSoldAndContainsAllSeatsNumbersFromCinemaHall() {
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(Set.of((short) 1, (short) 2, (short) 4, (short) 5))
                    .timeTableId(1015L)
                    .orderPrice(null)
                    .build();

            final OrderHumanDto saved = assertDoesNotThrow(() -> orderService.createOrder(order));
            final TimeTableDto byId = tableService.getByIdEagerAsDto(saved.timeTableId());
            assertTrue(byId.isSold());
            assertTrue(byId.closedSeats().containsAll(byId.cinemaHall().seatsType().values().stream()
                    .flatMap(Collection::stream).collect(Collectors.toSet())));

        }

        @Test
        void createOrderOk() {
            final Set<Short> seats = Set.of((short) 1, (short) 4, (short) 5);
            final long timeTableId = 1019L;
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(seats)
                    .timeTableId(timeTableId)
                    .orderPrice(null)
                    .build();
            final OrderHumanDto orderDto = assertDoesNotThrow(() -> orderService.createOrder(order));
            assertTrue(orderDto.seats().containsAll(seats));
            assertNotNull(orderDto.orderPrice());
            assertTrue(orderDto.orderPrice() > 0);
            assertEquals(timeTableId, orderDto.timeTableId());
            final Optional<TimeTable> timeTable = tableService.getByIdOptionalLazy(timeTableId);
            assertTrue(timeTable.isPresent());
            assertTrue(timeTable.get().getClosedSeats().containsAll(seats));

        }

        @Test
        void createOrderEvaluateTotalPrice() {
            final Set<Short> seats = Set.of((short) 1, (short) 4, (short) 5);
            final long timeTableId = 1019L;
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(seats)
                    .timeTableId(timeTableId)
                    .orderPrice(null)
                    .build();
            final OrderHumanDto orderDto = assertDoesNotThrow(() -> orderService.createOrder(order));
            assertNotNull(orderDto.orderPrice());
            assertTrue(orderDto.orderPrice() > 0);
            assertEquals(timeTableId, orderDto.timeTableId());
            final TimeTable timeTable = assertDoesNotThrow(() -> tableService.getByIdEager(timeTableId));
            final int sum = seats.stream()
                    .mapToDouble(seat -> timeTable.getCinemaHall().getSeatTypeBySeatNumber(seat).getCoefficient())
                    .mapToInt(coef -> (int) Math.round(coef * timeTable.getBasePrice()))
                    .sum();
            assertEquals(sum, orderDto.orderPrice());

        }
    }

    @Nested
    @DisplayName("Delete order case")
    public class DeleteOrder {
        @Test
        void deleteOrder() {
            final long id = 137L;
            final OrderHumanDto orderBeforeRemove = orderService.getById(id);
            final Set<Short> seats = orderBeforeRemove.seats();
            final Long timeTableId = orderBeforeRemove.timeTableId();
            final TimeTable timeTableBeforeRemoveOrder = assertDoesNotThrow(() -> tableService.getByIdOptionalLazy(timeTableId)
                    .orElseThrow());
            assertTrue(timeTableBeforeRemoveOrder.getIsSold());
            assertTrue(timeTableBeforeRemoveOrder.getClosedSeats().containsAll(seats));
            final Boolean del = assertDoesNotThrow(() -> orderService.deleteOrder(id));
            assertTrue(del);
            assertThrowsExactly(EntityNotFoundException.class,
                    () -> orderService.getById(id),
                    String.format(OrderServiceImpl.ORDER_NOT_FOUND, id));
            final Optional<TimeTable> timeTable = tableService.getByIdOptionalLazy(timeTableId);
            assertTrue(timeTable.isPresent());
            assertFalse(seats.isEmpty());
            assertFalse(timeTable.get().getIsSold());
            assertFalse(timeTable.get().getClosedSeats().isEmpty());
            seats.forEach(seat -> assertFalse(timeTable.get().getClosedSeats().contains(seat)));

        }

        @Test
        void deleteOrderNotFound() {
            final long id = 94L;
            assertThrowsExactly(EntityNotFoundException.class,
                    () -> orderService.deleteOrder(id)
                    , String.format(OrderServiceImpl.ORDER_NOT_FOUND, id));
        }

        @Test
        void deleteOrderSessionAlreadyStarted() {
            final long id = 148L;
            assertThrowsExactly(IllegalStateException.class, () -> orderService.deleteOrder(id), "order.cannot.delete.session.started");
        }
    }
}