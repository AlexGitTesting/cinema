package com.example.cinema.service;

import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.TimeTableDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
class TimeTableServiceImplTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TimeTableService service;

    @Test
    void getByFilerSingleMovie() {
        final TimeTableQueryFilter build = TimeTableQueryFilter.builder()
                .movieId(1000L)
                .limit(30)
                .page(0)
                .build();
        final List<TimeTableDto> content = service.getByFiler(build).getContent();
        assertTrue(content.size() >= 1);
        assertTrue(content.stream().allMatch(table -> table.movie().getId().orElseThrow() == 1000L));
    }


    @Test
    void getByFilerLazyInitialization() {
        final TimeTableQueryFilter build = TimeTableQueryFilter.builder()
                .movieId(1002L)
                .limit(2)
                .page(1)
                .dateSession(LocalDate.now().plusDays(1L))
                .build();
        final Page<TimeTableDto> byFiler = service.getByFiler(build);
        assertEquals(1, byFiler.getNumber());
        //lazy
        assertNotNull(byFiler.getContent().stream().findFirst().orElseThrow().cinemaHall().seatsAmount());
        assertNotNull(byFiler.getContent().stream().findFirst().orElseThrow().movie().getProducer());

    }

    @Test
    void getByFiler4() {
        final TimeTableQueryFilter build = TimeTableQueryFilter.builder().limit(30).page(0).build();
        final List<TimeTableDto> content = service.getByFiler(build).getContent();
        content.forEach(el -> log.info(el.toString()));
    }

    @Test
    void updateTimeTable() {
        final TimeTable timeTable = assertDoesNotThrow(() -> service.getByIdEager(1015L));
        final Set<Short> args = Set.of((short) 1, (short) 2, (short) 4, (short) 5);
        assertTrue(timeTable.addClosedSeats(args));
        final TimeTable saved = assertDoesNotThrow(() -> service.updateTimeTable(timeTable));
        assertTrue(saved.getClosedSeats().containsAll(args));
        assertTrue(saved.getIsSold());
    }
}