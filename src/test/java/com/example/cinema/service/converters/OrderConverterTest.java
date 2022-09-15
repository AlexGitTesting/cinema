package com.example.cinema.service.converters;

import com.example.cinema.domain.OrderTable;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Order convertert.
 *
 * @author Alexandr Yefremov
 */
class OrderConverterTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final OrderConverter converter = new OrderConverterImpl();

    @Test
    void toDto() {
        long id = 100;
        int price = 200;
        final Set<Short> shorts = Set.of((short) 5, (short) 6);
        String cust = "customer";
        final TimeTable timeTable = new TimeTable(100L);
        final OrderTable t = new OrderTable(id, timeTable, price, cust);
        t.setSeats(shorts);
        final OrderDto d = converter.toDto(t);
        assertEquals(id, d.getId().orElseThrow());
        assertEquals(price, d.getOrderPrice().orElseThrow());
        assertEquals(shorts, d.getSeats());
        assertEquals(timeTable.getId(), d.getTimeTableId());


    }

    @Test
    void toDomain() {
        long id = 100, tId = 200;
        int price = 200;
        final Set<Short> shorts = Set.of((short) 5, (short) 6);
        String cust = "customer";
        final OrderDto d = OrderDto.builder()
                .id(id)
                .orderPrice(price)
                .customer(cust)
                .timeTableId(tId)
                .seats(shorts)
                .build();
        final OrderTable t = converter.toDomain(d);
        log.info(t.toString());
        assertEquals(id, t.getId());
        assertEquals(tId, t.getTimeTable().getId());
        assertEquals(0, t.getOrderPrice().orElseThrow());
        assertEquals(shorts, t.getSeats());
        assertEquals(cust, t.getCustomer());
    }
}