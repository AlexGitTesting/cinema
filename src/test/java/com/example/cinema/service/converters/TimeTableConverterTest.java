package com.example.cinema.service.converters;

import com.example.cinema.core.SeatType;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.TimeTableDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test for TimeTableConverter.
 *
 * @author Alexandr Yefremov
 */
class TimeTableConverterTest {
    private final MovieConverter movieConverter = new MovieConverterImpl();
    private final CinemaHallConverter cinemaHallConverter = new CinemaHallConverterImpl();
    private final TimeTableConverter converter = new TimeTableConverterImpl(movieConverter, cinemaHallConverter);

    @Test
    void toDto() {
        //movie
        long idm = 105L, idT = 1000L;
        String titleM = "title";
        short timingM = 55;
        String producerM = "producer";
        final Movie movie = new Movie(idm, titleM, timingM, producerM);
        //cinemaHall
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        long ids = 100L;
        String names = "name";
        short amounts = 10;
        final CinemaHall hall = new CinemaHall(ids, names, amounts);
        hall.setSeatsType(seatsType);
        //timeTable
        final LocalDateTime date = LocalDateTime.of(2022, 1, 12, 1, 5);
        final short basePrice = (short) 55;
        final TimeTable timeTable = new TimeTable(idT, movie, hall, date, basePrice, false);
        final Set<Short> closed = Set.of((short) 1, (short) 2);
        timeTable.addClosedSeats(closed);
        final TimeTableDto dto = converter.toDto(timeTable);
        assertEquals(idT, dto.id());
        assertEquals(movie.getId(), dto.movie().getId().orElseThrow());
        assertEquals(hall.getId(), dto.cinemaHall().id());
        assertEquals(basePrice, dto.basePrice());
        assertEquals(false, dto.isSold());
        assertEquals(date, dto.startSession());
        assertEquals(closed, dto.closedSeats());


    }
}