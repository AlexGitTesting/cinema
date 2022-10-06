package com.example.cinema.dao;

import com.example.cinema.core.SeatType;
import com.example.cinema.domain.CinemaHall;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"classpath:statements/insert_cinema_hall.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:statements/truncate_cinema_hall.sql"})
class CinemaHallRepositoryTest extends BaseDataJpaTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CinemaHallRepository hallRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void save() {
        final CinemaHall red = new CinemaHall(null, "White", (short) 20);
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7, (short) 8, (short) 9, (short) 10)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 11, (short) 12, (short) 13, (short) 14, (short) 15, (short) 16
                , (short) 17, (short) 18, (short) 19, (short) 20)));
        red.setSeatsType(seatsType);
        final CinemaHall cinemaHall = assertDoesNotThrow(() -> hallRepository.saveAndFlush(red));
        log.info(cinemaHall.toString());
    }

    @Test
    void findById() {
        final CinemaHall cinemaHall = assertDoesNotThrow(() -> hallRepository.findById(100L).orElseThrow());
        assertNotNull(cinemaHall);
        assertNotNull(cinemaHall.getSeatsAmount());
        assertNotNull(cinemaHall.getName());
        assertEquals(100L, cinemaHall.getId());

    }

    @Test
    void update() {
        CinemaHall cinemaHall = assertDoesNotThrow(() -> hallRepository.findById(101L).orElseThrow());
        final String braun = "Braun";
        cinemaHall.setName(braun);
        cinemaHall = hallRepository.saveAndFlush(cinemaHall);
        assertEquals(braun, cinemaHall.getName());

    }

    @Test
    void delete() {
        hallRepository.deleteById(100L);
        entityManager.flush();
        assertThrows(NoSuchElementException.class, () -> hallRepository.findById(1000L).orElseThrow());
    }


}