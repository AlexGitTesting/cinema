package com.example.cinema.domain;

import com.example.cinema.core.SeatType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Timetable entity's methods.
 *
 * @author Alexandr Yefremov
 */
class TimeTableTest {
    private CinemaHall getCinema() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        final CinemaHall cinemaHall = new CinemaHall(100L, "RED", (short) 10);
        cinemaHall.setSeatsType(seatsType);
        return cinemaHall;
    }

    @Test
    void addClosedSeatsEmptyCollection() {
        CinemaHall cinemaHall = getCinema();
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(cinemaHall);
        timeTable.addClosedSeats(Set.of((short) 2, (short) 3));
        assertThrowsExactly(IllegalArgumentException.class, () -> timeTable.addClosedSeats(emptySet()), "Booked seats are empty");
    }

    @Test
    void addClosedSeatsDuplicatedSeats() {
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        timeTable.addClosedSeats(Set.of((short) 2, (short) 3));
        assertThrowsExactly(IllegalArgumentException.class
                , () -> timeTable.addClosedSeats(Set.of((short) 5, (short) 6, (short) 7, (short) 2)), "seats.already.closed");
    }

    @Test
    void closedSeatsAmountGreaterThenCinemaHallTotalAmount() {
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        timeTable.addClosedSeats(Set.of((short) 2, (short) 3));
        final Set<Short> set = Set.of((short) 1, (short) 11, (short) 12, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10);
        assertThrowsExactly(IllegalArgumentException.class, () ->
                timeTable.addClosedSeats(set), "Amount of the booked seats are greater then total amount of seats of the current cinema hall."
        );
        assertEquals(2, timeTable.getClosedSeats().size());
    }

    @Test
    void unsupportedOperationAddClosetSeats() {
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        assertThrowsExactly(UnsupportedOperationException.class, () -> timeTable.getClosedSeats().addAll(Set.of((short) 2, (short) 3)));
    }

    @Test
    void closedSeatsOutOfRange() {
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        timeTable.addClosedSeats(Set.of((short) 2, (short) 3));
        final Set<Short> set = Set.of((short) 1, (short) 11, (short) 12, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10);
    }

    @Test
    void isSoldIfAllTicketsSold() {
        final Set<Short> set = Set.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10);
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        timeTable.addClosedSeats(set);
        assertEquals(true, timeTable.getIsSold());

    }

    @Test
    void reopenSoldTicketsEmptyCandidates() {
        final Set<Short> set = Set.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10);
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        timeTable.addClosedSeats(set);
        assertTrue(timeTable.getIsSold());
        assertThrowsExactly(IllegalArgumentException.class, () -> timeTable.reopenClosedSeats(emptySet()), "Seats for cancelling booking are empty");
        assertTrue(timeTable.getIsSold());
    }

    @Test
    void reopenSoldTicketsTimeTableNotContainsAllCandidates() {
        final Set<Short> set = Set.of((short) 1, (short) 3, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10);
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        timeTable.addClosedSeats(set);
        assertThrowsExactly(IllegalArgumentException.class, () -> timeTable.reopenClosedSeats(Set.of((short) 2, (short) 3)), "Seats for cancel booking are not contained in the current timetable");
    }

    @Test
    void reopenSoldTicketsIsSoldToFalse() {
        final Set<Short> set = Set.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7, (short) 8, (short) 9, (short) 10);
        final TimeTable timeTable = new TimeTable(12L);
        timeTable.setCinemaHall(getCinema());
        timeTable.addClosedSeats(set);
        assertEquals(true, timeTable.getIsSold());
        assertDoesNotThrow(() -> timeTable.reopenClosedSeats(Set.of((short) 2, (short) 3)));
        assertFalse(timeTable.getIsSold());

    }

    @Test
    void setBasePriceNegativePrice() {
        final TimeTable timeTable = new TimeTable(12L);
        assertThrowsExactly(IllegalArgumentException.class, () -> timeTable.setBasePrice((short) -5));
    }

    @Test
    void setStartSessionEarlierThenNow() {
        final TimeTable timeTable = new TimeTable(12L);
        assertThrowsExactly(IllegalArgumentException.class, () -> timeTable.setStartSession(LocalDateTime.of(2022, 1, 12, 12, 17)), "Start session must not be earlier then now");
    }


}