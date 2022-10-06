package com.example.cinema.service;

import com.example.cinema.core.ValidationCustomException;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;
import com.example.cinema.dto.TimeTableDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

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
    void createOrderStartSessionInFuture() {
        final OrderDto order = OrderDto.builder()
                .customer("customer")
                .id(null)
                .seats(Set.of((short) 1, (short) 2, (short) 4))
                .timeTableId(1015L)
                .orderPrice(null)
                .build();

        final OrderDto orderSaved = assertDoesNotThrow(() -> orderService.createOrder(order));
        assertNotNull(orderSaved.getId());
        assertDoesNotThrow(() -> orderSaved.getOrderPrice().orElseThrow());

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
    void createOrderAllSeatsAreSold() {
        final OrderDto order = OrderDto.builder()
                .customer("customer")
                .id(null)
                .seats(Set.of((short) 1, (short) 2, (short) 4, (short) 5))
                .timeTableId(1015L)
                .orderPrice(null)
                .build();

        final OrderDto saved = assertDoesNotThrow(() -> orderService.createOrder(order));
        final TimeTableDto byId = tableService.getByIdEagerAsDto(saved.getTimeTableId());
        assertTrue(byId.isSold());

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
        final OrderDto orderDto = assertDoesNotThrow(() -> orderService.createOrder(order));
        assertTrue(orderDto.getSeats().containsAll(seats));
        assertTrue(orderDto.getOrderPrice().isPresent());
        assertTrue(orderDto.getOrderPrice().get() > 0);
        assertEquals(timeTableId, orderDto.getTimeTableId());
        final Optional<TimeTable> timeTable = tableService.getByIdOptionalLazy(timeTableId);
        assertTrue(timeTable.isPresent());
        assertTrue(timeTable.get().getClosedSeats().containsAll(seats));

    }

    @Test
    void deleteOrder() {
        final long id = 137L;
        final OrderDto orderBeforeRemove = orderService.getById(id);
        final Set<Short> seats = orderBeforeRemove.getSeats();
        final Long timeTableId = orderBeforeRemove.getTimeTableId();
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


    @Test
    void getById() {
        final OrderDto byId = orderService.getById(137L);
        assertEquals(1022L, byId.getTimeTableId());

    }
}