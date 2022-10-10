package com.example.cinema.service;

import com.example.cinema.core.ValidationCustomException;
import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.dto.MovieDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for movie repository
 *
 * @author Alexandr Yefremov
 */
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
class MovieServiceTest {
    @Autowired
    private MovieService service;

    @Test
    @DisplayName("Delete movie, when movie by id is not existed")
    void deleteNotFound() {
        assertThrowsExactly(EntityNotFoundException.class, () -> service.delete(1500L), "not.found.movie");

    }

    @Nested
    class GetByFilterMovie {
        @Test
        void getByFiler() {
            final MovieQueryFilter filter = MovieQueryFilter.builder().active(false).page(0).limit(10).build();
            final Page<MovieDto> movies = service.getByFiler(filter);
            assertTrue(movies.getTotalElements() >= 5);
            assertTrue(movies.stream().mapToLong(g -> g.getId().orElseThrow()).anyMatch(id -> id == 1004L || id == 1001L));

        }
    }

    @Nested
    class CreateMovie {

        @Test
        void create() {
            final MovieDto dto = MovieDto.builder().producer("Petro").title("Go to California").timing((short) 15).build();
            final MovieDto saved = service.create(dto);
            assertEquals(dto.getTitle(), saved.getTitle());
            assertEquals(dto.getProducer(), saved.getProducer());
            assertEquals(dto.getTiming(), saved.getTiming());
            assertNotNull(saved.getId());
        }

        @Test
        void createNotValidDto() {
            final MovieDto dto = MovieDto.builder().producer("  ").title("Go to California").timing((short) 15).build();
            assertThrowsExactly(ValidationCustomException.class, () -> service.create(dto));
        }
    }

    @Nested
    class GetById {
        @Test
        void getByIdFound() {
            final MovieDto dto = assertDoesNotThrow(() -> service.getById(1004L));
            assertEquals("Dark of the Moon", dto.getTitle().orElseThrow());
            assertEquals("Nelson Shin", dto.getProducer().orElseThrow());
            assertEquals((short) 102, dto.getTiming().orElseThrow());
        }

        @Test
        void getByIdNotFound() {
            assertThrows(EntityNotFoundException.class, () -> service.getById(2005L));
        }
    }

    @Nested
    class UpdateMovie {
        @Test
        void updateWithId() {
            final MovieDto dto = MovieDto.builder()
                    .producer("Petro")
                    .title("Go to California")
                    .timing((short) 15)
                    .id(1004L)
                    .build();
            final MovieDto updated = service.update(dto);
            assertEquals(dto.getId(), updated.getId());
            assertEquals(dto.getProducer(), updated.getProducer());
            assertEquals(dto.getTitle(), updated.getTitle());
            assertEquals(dto.getTiming(), updated.getTiming());
        }

        @Test
        void updateWithOutId() {
            final MovieDto dto = MovieDto.builder()
                    .producer("Petro")
                    .title("Go to California")
                    .timing((short) 15)
                    .build();
            final ValidationCustomException exception = assertThrowsExactly(ValidationCustomException.class, () -> service.update(dto));
            final Map<String, String> messageMap = exception.getMessageMap();
            assertTrue(messageMap.containsKey("id"));
            assertEquals("field.error.not.null", messageMap.get("id"));
        }
    }

    @Nested
    class DeleteMovie {
        @Test
        void deleteExistsTimeTable() {
            assertThrowsExactly(IllegalArgumentException.class, () -> service.delete(1003L));
        }

        @Test
        void deleteNotExistsTimeTable() {
            assertTrue(service.delete(1005L));
        }
    }
}