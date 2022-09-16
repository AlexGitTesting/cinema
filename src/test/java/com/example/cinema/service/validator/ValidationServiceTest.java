package com.example.cinema.service.validator;

import com.example.cinema.core.RequiredFieldsForCreation;
import com.example.cinema.core.RequiredFieldsForUpdating;
import com.example.cinema.core.SeatType;
import com.example.cinema.core.ValidationCustomException;
import com.example.cinema.dto.BasisTimeTable;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.dto.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidationServiceTest {
    @Autowired
    private ValidationService service;

    @Test
    void validateBasisTimeTableNulls() {
        final BasisTimeTable nulls = new BasisTimeTable(null, null, null, null);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(nulls, BasisTimeTable.class.getSimpleName()));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsValue("field.error.not.null"));
        assertTrue(map.containsKey("movieId"));
        assertTrue(map.containsKey("cinemaHallId"));
        assertTrue(map.containsKey("basePrice"));

    }

    @Test
    void validateBasisTimeTableIncompatibleValues() {
        final BasisTimeTable nulls = new BasisTimeTable(-1L, 0L, LocalDateTime.now(), (short) -3);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(nulls, BasisTimeTable.class.getSimpleName()));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsValue("field.error.min._1"));
        assertTrue(map.containsValue("field.error.min._0"));
        assertTrue(map.containsKey("movieId"));
        assertTrue(map.containsKey("cinemaHallId"));
        assertTrue(map.containsKey("basePrice"));
    }

    @Test
    void validationCinemaHallNulls() {
        final CinemaHallDto cinema = new CinemaHallDto(null, null, null, null);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName()));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsValue("field.error.not.null"));
        assertTrue(map.containsValue("field.error.blank"));
        assertTrue(map.containsValue("field.error.empty.collection"));
        assertTrue(map.containsKey("id"));
        assertTrue(map.containsKey("name"));
        assertTrue(map.containsKey("seatsAmount"));
        assertTrue(map.containsKey("seatsType"));
    }

    @Test
    void validationCinemaHallIncorrectFields() {
        final CinemaHallDto cinema = new CinemaHallDto(-1L, "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", (short) 0, new EnumMap(SeatType.class));
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName()));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsValue("field.error.min._1"));
        assertTrue(map.containsValue("field.error.size.incorrect"));
        assertTrue(map.containsValue("field.error.min._1"));
        assertTrue(map.containsValue("field.error.empty.collection"));
        assertTrue(map.containsKey("id"));
        assertTrue(map.containsKey("name"));
        assertTrue(map.containsKey("seatsAmount"));
        assertTrue(map.containsKey("seatsType"));
    }

    @Test
    void validateMovieDtoCreationNulls() {
        final MovieDto dto = MovieDto.builder().id(null).timing(null).title(null).producer(null).build();
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(dto, MovieDto.class.getSimpleName(), RequiredFieldsForCreation.class));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsKey("title"));
        assertTrue(map.containsKey("timing"));
        assertTrue(map.containsKey("producer"));
        assertFalse(map.containsKey("id"));
        assertTrue(map.containsValue("field.error.blank"));
        assertFalse(map.containsValue("field.error.min._1"));
        assertFalse(map.containsValue("field.error.size.incorrect"));
        assertTrue(map.containsValue("field.error.not.null"));
    }

    @Test
    void validateMovieDtoUpdating() {
        final MovieDto dto = MovieDto.builder().id(50L).timing((short) 75).title("null").producer("null").build();
        assertDoesNotThrow(() -> service.validate(dto, MovieDto.class.getSimpleName(), RequiredFieldsForUpdating.class));


    }
}