package com.example.cinema.service.converters;

import com.example.cinema.core.SeatType;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.OrderTable;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.OrderDto;
import com.example.cinema.dto.OrderHumanDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
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

    @Test
    void toHumanDto() {
        //movie
        final String movieTitle = "MovieTitle";
        final short timing = (short) 56;
        final String movieProducer = "MovieProducer";
        final Movie movie = new Movie(20L, movieTitle, timing, movieProducer);
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        //cinemaHall
        final long cinemaId = 59L;
        final String cinema_hall_name = "Cinema hall name";
        final short seatsAmount = (short) 10;
        final CinemaHall cinemaHall = new CinemaHall(cinemaId, cinema_hall_name, seatsAmount);
        cinemaHall.setSeatsType(seatsType);
        //timetable
        final long timeTableId = 100L;
        final LocalDateTime startSession = LocalDateTime.now();
        final short basePrice = (short) 25;
        final Boolean isSold = Boolean.FALSE;
        final TimeTable timeTable = new TimeTable(timeTableId, movie, cinemaHall, startSession, basePrice, isSold);
        //order
        long orderId = 100L;
        int orderPrice = 200;
        final Set<Short> bookedSeats = Set.of((short) 5, (short) 6);
        String customer = "customer";
        final OrderTable t = new OrderTable(orderId, timeTable, orderPrice, customer);
        t.setSeats(bookedSeats);
        final OrderHumanDto convertedOrder = converter.toHumanDto(t);
        assertEquals(movieTitle, convertedOrder.movieTitle());
        assertEquals(cinema_hall_name, convertedOrder.cinemaHallName());
        assertEquals(orderPrice, convertedOrder.orderPrice());
        assertEquals(startSession, convertedOrder.startSession());
        assertEquals(bookedSeats, convertedOrder.seats());
        assertEquals(customer, convertedOrder.customer());
        assertEquals(timeTableId, convertedOrder.timeTableId());
        assertEquals(orderId, convertedOrder.id());


    }
}