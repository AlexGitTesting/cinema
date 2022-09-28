package com.example.cinema.service.validator;

import com.example.cinema.core.RequiredFieldsForCreation;
import com.example.cinema.core.RequiredFieldsForUpdating;
import com.example.cinema.core.SeatType;
import com.example.cinema.core.ValidationCustomException;
import com.example.cinema.dto.BasisTimeTable;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.dto.MovieDto;
import com.example.cinema.dto.OrderDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

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
    void validationCinemaHallCreation() {
        final CinemaHallDto cinema = new CinemaHallDto(null, null, null, null);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName(), RequiredFieldsForCreation.class));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsValue("field.error.not.null"));
        assertTrue(map.containsValue("field.error.blank"));
        assertTrue(map.containsValue("field.error.empty.collection"));
        assertFalse(map.containsKey("id"));
        assertTrue(map.containsKey("name"));
        assertTrue(map.containsKey("seatsAmount"));
        assertTrue(map.containsKey("seatsType"));
    }

    @Test
    void validationCinemaHallCreationNameBlankSeatsEmpty() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        final CinemaHallDto cinema = new CinemaHallDto(null, "  ", (short) 5, seatsType);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName(), RequiredFieldsForCreation.class));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsValue("field.error.blank"));
        assertTrue(map.containsValue("field.error.empty.collection"));
        assertTrue(map.containsKey("name"));
        assertTrue(map.containsKey("seatsType"));
        assertEquals("field.error.blank", map.get("name"));
        assertEquals("field.error.empty.collection", map.get("seatsType"));
    }

    @Test
    void validationCinemaHallCreationIdNotNull() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        HashSet<Short> value = new HashSet<>(List.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
        seatsType.put(SeatType.KISSES, value);
        final CinemaHallDto cinema = new CinemaHallDto(2L, "Rose", (short) 5, seatsType);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName(), RequiredFieldsForCreation.class));
        final Map<String, String> map = ex.getMessageMap();
        assertFalse(map.containsValue("field.error.not.null"));
        assertFalse(map.containsValue("field.error.blank"));
        assertFalse(map.containsValue("field.error.empty.collection"));
        assertTrue(map.containsKey("id"));
        assertEquals("field.error.null", map.get("id"));
        assertFalse(map.containsKey("name"));
        assertFalse(map.containsKey("seatsAmount"));
        assertFalse(map.containsKey("seatsType"));
    }

    @Test
    void validationCinemaHallUpdateIdNull() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        HashSet<Short> value = new HashSet<>(List.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
        seatsType.put(SeatType.KISSES, value);
        final CinemaHallDto cinema = new CinemaHallDto(null, "Rose", (short) 5, seatsType);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName(), RequiredFieldsForUpdating.class));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsKey("id"));
        assertEquals("field.error.not.null", map.get("id"));
        assertFalse(map.containsKey("name"));
        assertFalse(map.containsKey("seatsAmount"));
        assertFalse(map.containsKey("seatsType"));
    }

    @Test
    void validationCinemaHallUpdateIdLess1() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        HashSet<Short> value = new HashSet<>(List.of((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
        seatsType.put(SeatType.KISSES, value);
        final CinemaHallDto cinema = new CinemaHallDto(-3L, "Rose", (short) -3, seatsType);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName(), RequiredFieldsForUpdating.class));
        final Map<String, String> map = ex.getMessageMap();
        assertTrue(map.containsKey("id"));
        assertTrue(map.containsKey("seatsAmount"));
        assertEquals("field.error.min._1", map.get("id"));
        assertEquals("field.error.min._1", map.get("seatsAmount"));
    }

    @Test
    void validationCinemaHallIncorrectFields() {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        final CinemaHallDto cinema = new CinemaHallDto(-1L, "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj", (short) 0, seatsType);
        final ValidationCustomException ex = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(cinema, CinemaHallDto.class.getSimpleName(), RequiredFieldsForUpdating.class));
        final Map<String, String> map = ex.getMessageMap();
        assertEquals("field.error.min._1", map.get("id"));
        assertEquals("field.error.size.incorrect", map.get("name"));
        assertEquals("field.error.min._1", map.get("seatsAmount"));
        assertEquals("field.error.empty.collection", map.get("seatsType"));
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

    @Test
    void validateMovieDtoUpdatingIdNull() {
        final MovieDto dto = MovieDto.builder().id(null).timing((short) 75).title("null").producer("null").build();
        final ValidationCustomException exception = assertThrowsExactly(ValidationCustomException.class
                , () -> service.validate(dto, MovieDto.class.getSimpleName(), RequiredFieldsForUpdating.class));
        final Map<String, String> messageMap = exception.getMessageMap();
        assertTrue(messageMap.containsKey("id"));
        assertEquals("field.error.not.null", messageMap.get("id"));
    }

    @Test
    void validateOrder() {
        final OrderDto dto = OrderDto.builder().orderPrice(1).seats(Collections.emptySet()).customer("  ").timeTableId(null).id(2L).build();
        final ValidationCustomException exception = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(dto, OrderDto.class.getSimpleName()));
        final Map<String, String> map = exception.getMessageMap();
        assertTrue(map.containsKey("id"));
        assertTrue(map.containsKey("timeTableId"));
        assertTrue(map.containsKey("seats"));
        assertTrue(map.containsKey("customer"));
        assertTrue(map.containsValue("field.error.null"));
        assertTrue(map.containsValue("field.error.not.null"));
        assertFalse(map.containsValue("field.error.min._1"));
        assertTrue(map.containsValue("field.error.empty.collection"));

    }

    @Test
    void validateOrderMinTimTableId() {
        final OrderDto dto = OrderDto.builder().orderPrice(null).seats(Set.of((short) 3)).customer("Petro").timeTableId(-12L).id(null).build();
        final ValidationCustomException exception = assertThrowsExactly(ValidationCustomException.class, () -> service.validate(dto, OrderDto.class.getSimpleName()));
        final Map<String, String> map = exception.getMessageMap();
        assertFalse(map.containsKey("id"));
        assertTrue(map.containsKey("timeTableId"));
        assertFalse(map.containsKey("seats"));
        assertFalse(map.containsKey("customer"));
        assertFalse(map.containsValue("field.error.null"));
        assertTrue(map.containsValue("field.error.min._1"));
        assertFalse(map.containsValue("field.error.empty.collection"));

    }
}