package com.example.cinema.web;

import com.example.cinema.config.ProjectProperties;
import com.example.cinema.core.SeatType;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.service.CinemaHallServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Locale;

import static com.example.cinema.core.ValidatorHelper.INVALID_NUMBER;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
@AutoConfigureMockMvc
class CinemaHallControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ProjectProperties prop;

    @Test
    void createOk() throws Exception {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(null, name, amount, seatsType);
        final MockHttpServletRequestBuilder post = post(prop.getBase().getCinemaHall() + prop.getCreate())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        mockMvc.perform(post).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue(Long.class)))
                .andExpect(jsonPath("$.name", equalTo(name)))
                .andExpect(jsonPath("$.seatsType", notNullValue(SeatType.class)))
                .andExpect(jsonPath("$.seatsType.BLIND", notNullValue(SeatType.BLIND.getDeclaringClass())))
                .andExpect(jsonPath("$.seatsType.LUXURY", notNullValue(SeatType.LUXURY.getDeclaringClass())))
                .andExpect(jsonPath("$.seatsType.KISSES", notNullValue(SeatType.KISSES.getDeclaringClass())));

    }

    @Test
    void createNotUniqueName() throws Exception {
        final Locale locale = new Locale("en");
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "BLACK";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(null, name, amount, seatsType);
        final MockHttpServletRequestBuilder post = post(prop.getBase().getCinemaHall() + prop.getCreate())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto));
        final String content = mockMvc.perform(post).andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(content);
        final String message = messageSource.getMessage("cinema.hall.name.not.unique", new Object[]{dto.name()}, locale);
        assertTrue(message.contains(dto.name()));
        assertNotNull(message);
        assertTrue(message.equalsIgnoreCase(content));


    }

    @Test
    void createValidationErrorIdNotCorrect() throws Exception {
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(12L, name, amount, seatsType);
        final MockHttpServletRequestBuilder post = post(prop.getBase().getCinemaHall() + prop.getCreate())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)).locale(new Locale("uk"));
        final String content = mockMvc.perform(post).andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertTrue(content.toLowerCase().contains("id"));
        assertTrue(content.toLowerCase().contains("The field must be null".toLowerCase())
                || content.toLowerCase().contains("Поле повинно бути відсутнім".toLowerCase()));

    }

    @Test
    void updateNotValidDto() throws Exception {
        final Locale locale = new Locale("en");
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "    ";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(101L, name, amount, seatsType);
        final MockHttpServletRequestBuilder patch = patch(prop.getBase().getCinemaHall() + prop.getUpdate()).locale(locale)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .accept(APPLICATION_JSON);
        final String content = mockMvc.perform(patch).andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        assertTrue(content.contains("name"));
        final String message = messageSource.getMessage("field.error.blank", null, locale);
        assertTrue(content.contains(message));
    }

    @Test
    void updateCinemaHallHasSessionsInFuture() throws Exception {
        final Locale locale = new Locale("en");
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(101L, name, amount, seatsType);
        final MockHttpServletRequestBuilder patch = patch(prop.getBase().getCinemaHall() + prop.getUpdate()).contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .locale(locale)
                .content(objectMapper.writeValueAsString(dto));
        final String content = mockMvc.perform(patch).andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        assertNotNull(content);
        assertTrue(CinemaHallServiceImpl.CAN_NOT_UPDATE.equalsIgnoreCase(content));

    }

    @Test
    void updateCinemaHallNotFound() throws Exception {
        final Locale locale = new Locale("en");
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(110L, name, amount, seatsType);
        final MockHttpServletRequestBuilder patch = patch(prop.getBase().getCinemaHall() + prop.getUpdate()).contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .locale(locale)
                .content(objectMapper.writeValueAsString(dto));
        final String content = mockMvc.perform(patch).andDo(print()).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        assertNotNull(content);
        final String message = messageSource.getMessage("not.found.cinema.hall", null, locale);
        assertNotNull(message);
        assertTrue(message.equalsIgnoreCase(content));
    }

    @Test
    void updateOk() throws Exception {
        final Locale locale = new Locale("en");
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "name";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(104L, name, amount, seatsType);
        final MockHttpServletRequestBuilder patch = patch(prop.getBase().getCinemaHall() + prop.getUpdate()).contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .locale(locale)
                .content(objectMapper.writeValueAsString(dto));
        mockMvc.perform(patch)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(104L), Long.class))
                .andExpect(jsonPath("$.name", equalTo(name), String.class))
                .andExpect(jsonPath("$.seatsAmount", is((int) amount), Integer.class))
                .andExpect(jsonPath("$.seatsType.BLIND", notNullValue(SeatType.BLIND.getDeclaringClass())))
                .andExpect(jsonPath("$.seatsType.LUXURY", notNullValue(SeatType.LUXURY.getDeclaringClass())))
                .andExpect(jsonPath("$.seatsType.KISSES", notNullValue(SeatType.KISSES.getDeclaringClass())));

    }

    @Test
    void updateNotUniqueName() throws Exception {
        final Locale locale = new Locale("en");
        EnumMap<SeatType, HashSet<Short>> seatsType = new EnumMap<>(SeatType.class);
        seatsType.put(SeatType.BLIND, new HashSet<>(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5)));
        seatsType.put(SeatType.LUXURY, new HashSet<>(Arrays.asList((short) 6, (short) 7)));
        seatsType.put(SeatType.KISSES, new HashSet<>(Arrays.asList((short) 8, (short) 9, (short) 10)));
        String name = "BLACK";
        short amount = 10;
        final CinemaHallDto dto = new CinemaHallDto(104L, name, amount, seatsType);
        final MockHttpServletRequestBuilder patch = patch(prop.getBase().getCinemaHall() + prop.getUpdate()).contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .locale(locale)
                .content(objectMapper.writeValueAsString(dto));
        final String content = mockMvc.perform(patch)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(content);
        final String message = messageSource.getMessage("cinema.hall.name.not.unique", new Object[]{dto.name()}, locale);
        assertTrue(message.contains(dto.name()));
        assertNotNull(message);
        assertTrue(message.equalsIgnoreCase(content));


    }

    @Test
    void deleteAllow() throws Exception {
        final Locale locale = new Locale("en");
        final Long id = 104L;
        final MockHttpServletRequestBuilder del = delete(prop.getBase().getCinemaHall() + prop.getDelete(), id)
                .locale(locale);
        final String content = mockMvc.perform(del)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        final String message = messageSource.getMessage("cinema.hall.removed", new Object[]{id}, locale);
        assertNotNull(message);
        assertTrue(content.equalsIgnoreCase(message));

    }

    @Test
    void deleteNotValidParam() throws Exception {
        final MockHttpServletRequestBuilder delete = delete(prop.getBase().getCinemaHall() + prop.getDelete(), -13L)
                .locale(new Locale("en"));
        final String message = mockMvc.perform(delete)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();
        assertNotNull(message);
        assertTrue(message.equalsIgnoreCase(INVALID_NUMBER));

    }

    @Test
    void deleteNotAllow() throws Exception {
        final MockHttpServletRequestBuilder delete = delete(prop.getBase().getCinemaHall() + prop.getDelete(), 100L)
                .locale(new Locale("en"));
        final String content = mockMvc.perform(delete)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(content);
        assertTrue(content.equalsIgnoreCase(CinemaHallServiceImpl.CAN_NOT_DELETE));

    }

    @Test
    void getByIdNotValidParam() throws Exception {
        final MockHttpServletRequestBuilder get = get(prop.getBase().getCinemaHall() + prop.getGetById(), -3L)
                .locale(new Locale("uk"));
        final String message = mockMvc.perform(get)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();
        assertEquals(INVALID_NUMBER, message);

    }

    @Test
    void getByIdOk() throws Exception {
        final MockHttpServletRequestBuilder get = get(prop.getBase().getCinemaHall() + prop.getGetById(), 101L)
                .locale(new Locale("uk"));
        mockMvc.perform(get)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(101L), Long.class))
                .andExpect(jsonPath("$.name", notNullValue(String.class)))
                .andExpect(jsonPath("$.seatsAmount", notNullValue(Short.class)));


    }

    @Test
    void getByIdNotFound() throws Exception {
        final Locale locale = new Locale("en");
        final MockHttpServletRequestBuilder get = get(prop.getBase().getCinemaHall() + prop.getGetById(), 3000L)
                .locale(locale);
        final String content = mockMvc.perform(get)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        final String message = messageSource.getMessage("not.found.cinema.hall", null, locale);
        assertTrue(message.equalsIgnoreCase(content));
    }
}