package com.example.cinema.domain;

import com.example.cinema.core.SeatType;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link CinemaHall} methods.
 *
 * @author Alexandr Yefremov
 */
class CinemaHallTest {
    private EnumMap<SeatType, HashSet<Short>> getSeatsTypes() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7, (short) 8, (short) 9, (short) 10)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 11, (short) 12, (short) 13, (short) 14, (short) 15, (short) 16
                , (short) 17, (short) 18, (short) 19, (short) 20)));
        return seatsType;
    }

    @Test
    void getSeatsType() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        assertThrowsExactly(UnsupportedOperationException.class,
                () -> red.getSeatsType().put(SeatType.BLIND, new HashSet<>(Collections.singletonList((short) 21))));
    }

    @Test
    void setSeatsTypeEmpty() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        assertThrowsExactly(IllegalArgumentException.class
                , () -> red.setSeatsType(seatsType), "Seats amount must be not empty");
    }

    @Test
    void getSeatsTypeValidationDuplicatesOfSeats() {
        final EnumMap<SeatType, HashSet<Short>> seatsTypes = getSeatsTypes();
        seatsTypes.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 8, (short) 3, (short) 4, (short) 5)));
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        assertThrowsExactly(IllegalArgumentException.class
                , () -> red.setSeatsType(seatsTypes), "Seats type contains duplicate seats numbers");
    }

    @Test
    void getSeatsTypeValidationSeatAmountDoNotMuchAddedSeatsTypesMore() {
        final EnumMap<SeatType, HashSet<Short>> seatsTypes = getSeatsTypes();
        seatsTypes.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 21, (short) 22)));
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        assertThrowsExactly(IllegalArgumentException.class
                , () -> red.setSeatsType(seatsTypes)
                , " Amount of seats transferred to set seats type, do not match the total amount of seats for current cinema hall.");
    }

    @Test
    void getSeatsTypeValidationSeatAmountDoNotMuchAddedSeatsTypesLess() {
        final EnumMap<SeatType, HashSet<Short>> seatsTypes = getSeatsTypes();
        seatsTypes.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4)));
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        assertThrowsExactly(IllegalArgumentException.class
                , () -> red.setSeatsType(seatsTypes)
                , " Amount of seats transferred to set seats type, do not match the total amount of seats for current cinema hall.");
    }

    @Test
    void areSeatsRelatedToCurrentHallEmpty() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        red.setSeatsType(getSeatsTypes());
        assertThrowsExactly(IllegalArgumentException.class
                , () -> red.areSeatsRelatedToCurrentHall(Collections.emptySet())
                , " Candidates must be not null or not empty");
    }

    @Test
    void areSeatsRelatedToCurrentYes() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        red.setSeatsType(getSeatsTypes());
        assertTrue(red.areSeatsRelatedToCurrentHall(Set.of((short) 1, (short) 2, (short) 8, (short) 19)));

    }

    @Test
    void areSeatsRelatedToCurrentNo() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        red.setSeatsType(getSeatsTypes());
        assertFalse(red.areSeatsRelatedToCurrentHall(Set.of((short) 1, (short) 2, (short) 8, (short) 21)));
    }

    @Test
    void getSeatTypeBySeatNumberYest() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        red.setSeatsType(getSeatsTypes());
        assertEquals(SeatType.BLIND, red.getSeatTypeBySeatNumber((short) 5));
    }

    @Test
    void getSeatTypeBySeatNumberNet() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        red.setSeatsType(getSeatsTypes());
        assertThrowsExactly(IllegalArgumentException.class, () -> red.getSeatTypeBySeatNumber((short) 24), "Seat type by  number not found");
    }

    @Test
    void getSeatTypeBySeatNumberNull() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        red.setSeatsType(getSeatsTypes());
        assertThrowsExactly(IllegalArgumentException.class, () -> red.getSeatTypeBySeatNumber(null), "Invalid seat number");
    }

    @Test
    void getSeatTypeBySeatNumberNegative() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        red.setSeatsType(getSeatsTypes());
        assertThrowsExactly(IllegalArgumentException.class, () -> red.getSeatTypeBySeatNumber((short) -2), "Invalid seat number");
    }
}