package com.example.cinema.service.converters;

import com.example.cinema.core.SeatType;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.dto.CinemaHallDto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for CinemaHallConverter
 *
 * @author Alexandr Yefremov
 */
class CinemaHallConverterTest {
    private final CinemaHallConverter converter = new CinemaHallConverterImpl();

    @Test
    void toDto() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        long id = 100L;
        String name = "name";
        short amount = 10;
        final CinemaHall h = new CinemaHall(id, name, amount);
        h.setSeatsType(seatsType);
        final CinemaHallDto d = converter.toDto(h);
        assertEquals(id, d.id());
        assertEquals(name, d.name());
        assertEquals(amount, d.seatsAmount());
        assertEquals(h.getSeatsAmount(), d.seatsAmount());
    }
}