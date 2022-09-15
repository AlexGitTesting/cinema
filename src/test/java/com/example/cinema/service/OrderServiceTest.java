package com.example.cinema.service;

import com.example.cinema.dto.OrderDto;
import com.example.cinema.dto.TimeTableDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
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

        final IllegalStateException orderSaved = assertThrowsExactly(IllegalStateException.class, () -> orderService.createOrder(order));
        assertEquals("OrderDto must be not null and its is must be null", orderSaved.getMessage());

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
        assertEquals("You can not create order for movie that has already started", orderSaved.getMessage());

    }

    @Test
    void createOrderNullDto() {
        assertThrowsExactly(IllegalStateException.class, () -> orderService.createOrder(null));

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
        assertEquals("TimeTable not found by id = " + timeTableId, exc.getMessage());

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
        assertEquals("Seat type by  number not found", exc.getMessage());

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
    void deleteOrder() {
    }

    @Test
    void getById() {
    }
}