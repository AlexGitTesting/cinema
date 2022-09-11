package com.example.cinema.dao;

import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.TimeTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    private TimeTableRepository repository;
//    @Autowired
//    private TimeTableSpecificationImpl specification;


    @Test
    @Transactional
    void getById() {
        assertDoesNotThrow(() -> repository.findById(1007L).orElseThrow());
    }

    @Test
    @Transactional
    void deleteById() {
        repository.deleteById(1002L);
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
                .dateSession(LocalDate.now())
                .movieId(movieId).build();
        final List<TimeTable> all = repository.findAll(new TimeTableSpecificationImpl().getByFilter(build));
        assertFalse(all.isEmpty());
        assertTrue(all.stream().allMatch(timeTable -> timeTable.getMovie().getId().equals(movieId)));

    }

    @Test
    @Transactional
    void getByFilter1() {
        final long movieId = 1001L;
        final TimeTableQueryFilter build = TimeTableQueryFilter.builder()
                .movieId(1001L).build();
        final List<TimeTable> all = repository.findAll(new TimeTableSpecificationImpl().getByFilter(build));
        assertFalse(all.isEmpty());
        assertTrue(all.stream().allMatch(timeTable -> timeTable.getMovie().getId().equals(movieId)));

    }

    @Test
    @Transactional
    void save() {
        final TimeTable timeTable = new TimeTable(null, new Movie(1000L, "title", (short) 56, "prod"), new CinemaHall(100L, "name", (short) 45), LocalDateTime.now(), (short) 75, false);
        timeTable.getClosedSeats().add((short) 1);
        timeTable.getClosedSeats().add((short) 3);
        timeTable.getClosedSeats().add((short) 5);
        repository.save(timeTable);
    }


}