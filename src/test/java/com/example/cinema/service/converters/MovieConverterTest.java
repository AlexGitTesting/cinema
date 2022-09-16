package com.example.cinema.service.converters;

import com.example.cinema.domain.Movie;
import com.example.cinema.dto.MovieDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test movie converter.
 *
 * @author Alexandr Yefremov
 */
class MovieConverterTest {

    private final MovieConverter converter = new MovieConverterImpl();

    @Test
    void toDto() {
        long id = 100L;
        String title = "title";
        short timing = 55;
        String producer = "producer";
        final Movie source = new Movie(id, title, timing, producer);
        final MovieDto target = converter.toDto(source);
        assertEquals(id, target.getId().orElseThrow());
        assertEquals(timing, target.getTiming().orElseThrow());
        assertEquals(title, target.getTitle().orElseThrow());
        assertEquals(producer, target.getProducer().orElseThrow());
    }

    @Test
    void toDomain() {
        long id = 100L;
        String title = "title";
        short timing = 55;
        String producer = "producer";
        final MovieDto dto = MovieDto.builder().id(id).title(title).timing(timing).producer(producer).build();
        final Movie domain = converter.toDomain(dto);
        assertEquals(id, domain.getId());
        assertEquals(timing, domain.getTiming());
        assertEquals(title, domain.getTitle());
        assertEquals(producer, domain.getProducer());
    }
}