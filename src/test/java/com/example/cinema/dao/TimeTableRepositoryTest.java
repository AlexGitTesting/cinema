package com.example.cinema.dao;

import com.example.cinema.core.SeatType;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.TimeTable;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
class TimeTableRepositoryTest extends BaseDataJpaTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TimeTableRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void getById() {
        assertDoesNotThrow(() -> repository.findById(1007L).orElseThrow());
    }

    @Test
    @Transactional
    void getByIdNotFount() {
        assertThrowsExactly(NoSuchElementException.class, () -> repository.findById(10007L).orElseThrow());
    }

    @Test
    @Transactional
    void deleteById() {
        repository.deleteById(1002L);
        entityManager.flush();
        assertTrue(() -> repository.findById(1002L).isEmpty());

    }

    @Test
    @Transactional
    void updateById() {
        final TimeTable timeTable = repository.findById(1007L).orElseThrow();
        timeTable.setBasePrice((short) 123);
        final TimeTable save = repository.save(timeTable);
        assertEquals((short) 123, save.getBasePrice());
    }

    @Test
    @Transactional
    void getByFilter() {
        final long movieId = 1001L;
        final TimeTableQueryFilter build = TimeTableQueryFilter.builder()
                .dateSession(LocalDate.now().plusDays(1L))
                .movieId(movieId)
                .build();
        final List<TimeTable> all = repository.findAll(new TimeTableSpecificationImpl().getByFilter(build));
        assertFalse(all.isEmpty());
        assertTrue(all.stream().allMatch(timeTable -> timeTable.getMovie().getId().equals(movieId)));

    }

    @Test
    @Transactional
    void getByFilter1() {
        final long movieId = 1001L;
        final TimeTableQueryFilter build = TimeTableQueryFilter.builder()
                .movieId(1001L)
                .build();
        final List<TimeTable> all = repository.findAll(new TimeTableSpecificationImpl().getByFilter(build));
        assertFalse(all.isEmpty());
        assertTrue(all.stream().allMatch(timeTable -> timeTable.getMovie().getId().equals(movieId)));

    }

    @Test
    @Transactional
    void save() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        final CinemaHall cinemaHall = new CinemaHall(100L, "g", (short) 10);
        cinemaHall.setSeatsType(seatsType);
        final TimeTable timeTable = new TimeTable(null, new Movie(1000L, "title", (short) 56, "prod"), cinemaHall, LocalDateTime.now(), (short) 75, false);
        timeTable.addClosedSeats(Set.of((short) 3, (short) 1, (short) 5));
        repository.save(timeTable);
    }

    @Test
    @Transactional
    void updateIsClosedTrue() {
        final TimeTable timeTable = assertDoesNotThrow(() -> repository.getTimeTableByIdEager(1015L).orElseThrow());
        final Set<Short> args = Set.of((short) 1, (short) 2, (short) 4, (short) 5);
        timeTable.addClosedSeats(args);
        final TimeTable timeTable1 = assertDoesNotThrow(() -> repository.saveAndFlush(timeTable));
        entityManager.persist(timeTable1);
        entityManager.flush();
        assertTrue(timeTable1.getClosedSeats().containsAll(args));
        assertTrue(timeTable1.getIsSold());
    }

    @Test
    @Transactional
    void getTimeTableByIdEager() {
        assertDoesNotThrow(() -> repository.getTimeTableByIdEager(1015L).orElseThrow());
    }

    @Test
    @Transactional
    void getByFilterOld() {
        final TimeTableQueryFilter build = TimeTableQueryFilter.builder()
                .dateSession(LocalDate.now())
                .build();
        final List<TimeTable> all = repository.findAll(new TimeTableSpecificationImpl().getByFilter(build));
        final Set<TimeTable> collect = all.stream()
                .filter(a -> a.getStartSession().isBefore(LocalDateTime.now()))
                .collect(Collectors.toSet());
        collect.forEach(g -> log.info(g.toString()));

    }

    @Test
    @Transactional
    void ifTimeTableExistsByMovieIdInFuture() {
        assertTrue(repository.ifTimeTableExistsByMovieIdInFuture(1002L));
    }

    @Test
    @Transactional
    void ifTimeTableExistsByCinemaHallIdInFuture() {
        assertTrue(repository.ifTimeTableExistsByCinemaHallIdInFuture(100L));
    }
}