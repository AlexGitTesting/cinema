package com.example.cinema.dao;

import com.example.cinema.domain.BaseEntity;
import com.example.cinema.domain.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"classpath:statements/insert_movie.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:statements/truncate_movie.sql"})
class MovieRepositoryTest extends BaseDataJpaTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MovieRepository movieRepository;

    private final MovieSpecification specification = new MovieSpecificationImpl();
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void getMovieById() {
        final Optional<Movie> byId = movieRepository.findById(1000L);
        final Movie movie = assertDoesNotThrow((ThrowingSupplier<Movie>) byId::orElseThrow);
        assertEquals(1000L, movie.getId());
        assertEquals("Interstellar", movie.getTitle());
        assertEquals((short) 75, movie.getTiming());
    }

    @Test
    void getMovieByIdNotFound() {
        final Optional<Movie> byId = movieRepository.findById(10L);
        assertThrowsExactly(NoSuchElementException.class, byId::orElseThrow);

    }

    @Test
    void saveMovie() {
        final Movie movie = new Movie(null, "Batman Begins", (short) 75, "Christopher Nolan");
        final Movie movie1 = movieRepository.saveAndFlush(movie);
        assertNotNull(movie1.getId());
        assertTrue(movie1.getId() > 0);
    }

    @Test
    void updateMovie() {
        final String newTitle = "Dunkirk";
        final Optional<Movie> byId = movieRepository.findById(1000L);
        final Movie movie = assertDoesNotThrow((ThrowingSupplier<Movie>) byId::orElseThrow);
        movie.setTitle(newTitle);
        final Movie updated = movieRepository.saveAndFlush(movie);
        entityManager.detach(updated);
        final Movie refreshed = movieRepository.findById(1000L).orElseThrow();
        assertEquals(newTitle, refreshed.getTitle());
    }

    @Test
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
    void getByFilerContainsNotActiveMovies() {
        final MovieQueryFilter filter = MovieQueryFilter.builder().isActive(false).page(0).limit(10).build();
        final List<Movie> movies = movieRepository.findAll(specification.getByFilter(filter));
        assertTrue(movies.size() >= 5);
        assertTrue(movies.stream().mapToLong(BaseEntity::getId).anyMatch(id -> id == 1004L));
        assertTrue(movies.stream().mapToLong(BaseEntity::getId).anyMatch(id -> id == 1001L));

    }

    @Test
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
    void getByFilerContainsActiveMoviesOnly() {
        final MovieQueryFilter filter = MovieQueryFilter.builder().isActive(true).page(0).limit(10).build();
        final List<Movie> movies = movieRepository.findAll(specification.getByFilter(filter));
        assertTrue(movies.size() >= 3);
        assertTrue(movies.stream().mapToLong(BaseEntity::getId).noneMatch(id -> id == 1005L));

    }

    @Test
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
    void getByFilerByTitle() {
        final MovieQueryFilter filter = MovieQueryFilter.builder().title("Int").page(0).limit(10).build();
        final List<Movie> movies = movieRepository.findAll(specification.getByFilter(filter));
        assertFalse(movies.isEmpty());
        assertTrue(movies.stream().mapToLong(BaseEntity::getId).anyMatch(id -> id == 1000L));

    }

    @Test
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
    void getByFilerByProd() {
        final MovieQueryFilter filter = MovieQueryFilter.builder().producer("son").page(0).limit(10).build();
        final List<Movie> movies = movieRepository.findAll(specification.getByFilter(filter));
        assertFalse(movies.isEmpty());
        assertTrue(movies.stream().mapToLong(BaseEntity::getId).anyMatch(id -> id == 1003L));
        assertTrue(movies.stream().mapToLong(BaseEntity::getId).anyMatch(id -> id == 1004L));

    }

    @Test
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
    void getByFilerPaginationNotThrow() {
        final MovieQueryFilter filter = MovieQueryFilter.builder().isActive(false).page(1).limit(2).build();
        final Page<Movie> movies = assertDoesNotThrow(() -> movieRepository.findAll(specification.getByFilter(filter), PageRequest.of(filter.getPage(), filter.getLimit())));
        assertFalse(movies.getContent().isEmpty());
        movies.forEach(m -> log.info(m.toString()));

    }
}