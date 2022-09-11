package com.example.cinema.dto;

import com.example.cinema.dao.MovieRepository;
import com.example.cinema.domain.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void getMovieById() {
        final Optional<Movie> byId = movieRepository.findById(1000L);
        final Movie movie = assertDoesNotThrow((ThrowingSupplier<Movie>) byId::orElseThrow);
        assertEquals(1000L, movie.getId());
        assertEquals("Interstellar", movie.getTitle());
        assertEquals((short) 90, movie.getTiming());
    }

    @Test
    @Transactional
    void saveMovie() {
        final Movie movie = new Movie(null, "Batman Begins", (short) 75, "Christopher Nolan");
        final Movie movie1 = movieRepository.saveAndFlush(movie);
        assertNotNull(movie1.getId());
        assertTrue(movie1.getId() > 0);
    }

    @Test
    @Transactional
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
    @Transactional
    void deleteMovie() {
        movieRepository.deleteById(1000L);
        entityManager.flush();
        assertThrows(NoSuchElementException.class, () -> movieRepository.findById(1000L).orElseThrow());
    }


}