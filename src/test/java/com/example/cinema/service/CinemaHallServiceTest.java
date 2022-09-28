package com.example.cinema.service;

import com.example.cinema.core.SeatType;
import com.example.cinema.dto.CinemaHallDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@Sql({
        "classpath:statements/insert_cinema_hall.sql",
        "classpath:statements/insert_movie.sql",
        "classpath:statements/insert_time_table.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:statements/truncate_time_table.sql",
        "classpath:statements/truncate_movie.sql",
        "classpath:statements/truncate_cinema_hall.sql"
})
@SpringBootTest
class CinemaHallServiceTest {
    @Autowired
    private CinemaHallService service;


    @Test
    void createCinemaHall() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(null, name, amount, seatsType);
        final CinemaHallDto saved = assertDoesNotThrow(() -> service.createCinemaHall(dto));
        assertNotNull(saved.id());
        assertEquals(amount, dto.seatsAmount());
        assertEquals(seatsType, dto.seatsType());
    }

    @Test
    void createCinemaHallIncorrectSeatsAmount() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 11;
        final CinemaHallDto dto = new CinemaHallDto(null, name, amount, seatsType);
        final IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> service.createCinemaHall(dto));
        assertEquals("seats.not.match.total.amount", exception.getMessage());

    }

    @Test
    void createCinemaHallSeatsContainsDuplicatedSeatsNumbers() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 4)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 11;
        final CinemaHallDto dto = new CinemaHallDto(null, name, amount, seatsType);
        final IllegalArgumentException exception = assertThrowsExactly(IllegalArgumentException.class, () -> service.createCinemaHall(dto));
        assertEquals("seats.contain.duplicates", exception.getMessage());

    }

    @Test
    void readById() {
        final CinemaHallDto cinemaHallDto = assertDoesNotThrow(() -> service.readById(101L));
        assertEquals(101L, cinemaHallDto.id());
        assertEquals("YELLOW", cinemaHallDto.name());
        assertEquals((short) 12, cinemaHallDto.seatsAmount());
    }

    @Test
    void readByIdNotFound() {
        assertThrowsExactly(EntityNotFoundException.class, () -> service.readById(1001L));
    }

    @Test
    void update() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(104L, name, amount, seatsType);
        final CinemaHallDto saved = assertDoesNotThrow(() -> service.update(dto));
        assertEquals(amount, saved.seatsAmount());
        assertEquals(seatsType, saved.seatsType());
        assertEquals(104, saved.id());

    }

    @Test
    void deleteAllow() {
        assertDoesNotThrow(() -> service.delete(104L));

    }

    @Test
    void deleteNOTAllow() {
        assertThrowsExactly(IllegalArgumentException.class, () -> service.delete(101L), "You can not delete cinema hall, because there are timetables references on this cinema hall");
    }
}