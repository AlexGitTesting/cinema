package com.example.cinema.service;

import com.example.cinema.core.ValidatorHelper;
import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.dao.TimeTableRepository;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.BasisTimeTable;
import com.example.cinema.dto.TimeTableDto;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
class TimeTableServiceTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private TimeTableService service;
    @SpyBean(reset = MockReset.BEFORE)
    private TimeTableRepository spyRepoTimeTable;

    // create new
    @Test
    void createNewDtoValidator() {
        clearInvocations(spyRepoTimeTable);
        final LocalDateTime date = LocalDateTime.of(2023, 2, 12, 12, 12);
        final BasisTimeTable table = new BasisTimeTable(1000L, 100L
                , date, (short) 35);
        final TimeTableDto saved = service.createNew(table);
        assertEquals(table.movieId(), saved.movie().getId().orElseThrow());
        assertEquals(table.cinemaHallId(), saved.cinemaHall().id());
        assertEquals(date, saved.startSession());
        assertEquals((short) 35, saved.basePrice());
        verify(spyRepoTimeTable, only()).save(any());

    }

    @Test
    void createTimeTableSessionNotCorrect() {
        clearInvocations(spyRepoTimeTable);
        final BasisTimeTable table = new BasisTimeTable(1000L, 100L
                , LocalDateTime.of(2022, 2, 12, 12, 12), (short) 35);
        assertThrowsExactly(IllegalArgumentException.class, () -> service.createNew(table), "start.session.not.correct");
        verify(spyRepoTimeTable, never()).save(any());
    }

    @Test
    void createNewValidateIdsMovieNotExists() {
        clearInvocations(spyRepoTimeTable);
        final BasisTimeTable table = new BasisTimeTable(2000L, 100L
                , LocalDateTime.of(2023, 2, 12, 12, 12), (short) 35);
        assertThrowsExactly(EntityNotFoundException.class, () -> service.createNew(table), "not.found.movie");
        verify(spyRepoTimeTable, never()).save(any());
    }

    @Test
    void createNewValidateCinemaNotExists() {
        clearInvocations(spyRepoTimeTable);
        final BasisTimeTable table = new BasisTimeTable(2000L, 1000L
                , LocalDateTime.of(2023, 2, 12, 12, 12), (short) 35);
        assertThrowsExactly(EntityNotFoundException.class, () -> service.createNew(table), "not.found.cinema.hall");
        verify(spyRepoTimeTable, never()).save(any());
    }

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

    @Test
    void updateTimeTableValidateId() {
        final TimeTable timeTable = assertDoesNotThrow(() -> service.getByIdEager(1015L));
        final Set<Short> args = Set.of((short) 1, (short) 2, (short) 4, (short) 5);
        assertTrue(timeTable.addClosedSeats(args));
        timeTable.setId(-3L);
        assertThrowsExactly(IllegalArgumentException.class, () -> service.updateTimeTable(timeTable), ValidatorHelper.INVALID_NUMBER);
    }

    @Test
    void updateTimeTableValidateMovieId() {
        final TimeTable timeTable = assertDoesNotThrow(() -> service.getByIdEager(1015L));
        final Set<Short> args = Set.of((short) 1, (short) 2, (short) 4, (short) 5);
        assertTrue(timeTable.addClosedSeats(args));
        timeTable.setMovie(new Movie(-3L));
        assertThrowsExactly(IllegalArgumentException.class, () -> service.updateTimeTable(timeTable), ValidatorHelper.INVALID_NUMBER);
    }

    @Test
    void updateTimeTableValidateCinemaHallId() {
        final TimeTable timeTable = assertDoesNotThrow(() -> service.getByIdEager(1015L));
        final Set<Short> args = Set.of((short) 1, (short) 2, (short) 4, (short) 5);
        assertTrue(timeTable.addClosedSeats(args));
        timeTable.setCinemaHall(new CinemaHall(-3L));
        assertThrowsExactly(IllegalArgumentException.class, () -> service.updateTimeTable(timeTable), ValidatorHelper.INVALID_NUMBER);
    }

    @Test
    void ifTimeTableExistsByCinemaHallIdInFutureIdNotCorrect() {
        clearInvocations(spyRepoTimeTable);
        assertThrowsExactly(IllegalArgumentException.class, () -> service.ifTimeTableExistsByCinemaHallIdInFuture(-3L), "Argument is invalid");
        verify(spyRepoTimeTable, never()).ifTimeTableExistsByCinemaHallIdInFuture(anyLong());
    }

    @Test
    void ifTimeTableExistsByCinemaHallIdInFutureIdCorrect() {
        clearInvocations(spyRepoTimeTable);
        final boolean b = service.ifTimeTableExistsByCinemaHallIdInFuture(100L);
        assertTrue(b);
        verify(spyRepoTimeTable, only()).ifTimeTableExistsByCinemaHallIdInFuture(100L);

    }

    @Test
    void getByIdOptionalLazy() {
        final Optional<TimeTable> time = service.getByIdOptionalLazy(1004L);
        assertTrue(time.isPresent());
        assertThrowsExactly(LazyInitializationException.class, () -> {
            short amount = time.get().getCinemaHall().getSeatsAmount();
        });
    }
}