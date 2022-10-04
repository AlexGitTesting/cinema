package com.example.cinema.web;

import com.example.cinema.config.ProjectProperties;
import com.example.cinema.core.ValidatorHelper;
import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.dto.BasisTimeTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
@AutoConfigureMockMvc
@SpringBootTest
class TimetableControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProjectProperties prop;

    @Test
    void createNotValidDto() throws Exception {
        final LocalDateTime date = LocalDateTime.now().plusDays(3);
        final BasisTimeTable table = new BasisTimeTable(-3L, 100L
                , date, (short) 35);
        final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getCreate()).accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(table));
        final String content = mockMvc.perform(post)
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
        assertTrue(content.toLowerCase().contains("Validation exception".toLowerCase())
                && content.toLowerCase().contains("movieId".toLowerCase()));

    }

    @Test
    void createValidDto() throws Exception {
        final LocalDateTime date = LocalDateTime.now().plusDays(3);
        final BasisTimeTable table = new BasisTimeTable(1000L, 100L
                , date, (short) 35);
        final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getCreate()).accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(table));
        mockMvc.perform(post)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void getByIdInvalidId() throws Exception {
        final MockHttpServletRequestBuilder get = get(prop.getBase().getTimeTable() + prop.getGetById(), -3L);
        final String errorMessage = mockMvc.perform(get)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getErrorMessage();
        assertEquals(errorMessage, ValidatorHelper.INVALID_NUMBER);
    }

    @Test
    void getById() throws Exception {
        final MockHttpServletRequestBuilder get = get(prop.getBase().getTimeTable() + prop.getGetById(), 1000L);
        mockMvc.perform(get)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movie.title", containsStringIgnoringCase("Batman Begins")))
                .andExpect(jsonPath("$.id", is(1000L), Long.class))
                .andExpect(jsonPath("$.cinemaHall.name", containsStringIgnoringCase("BLACK")))
                .andExpect(jsonPath("$.closedSeats", hasItems(3, 5, 10)));

    }

    @Test
    void getByFilter() throws Exception {
        final TimeTableQueryFilter filter = TimeTableQueryFilter.builder().movieId(1001L).build();
        final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getGetByFilter())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(filter));
        mockMvc.perform(post)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*].movie.id", everyItem(is(1001))))
                .andExpect(jsonPath("$.content[*].movie.title", everyItem(containsStringIgnoringCase("Batman Begins"))))
                .andExpect(jsonPath("$.content[*].cinemaHall.name", everyItem(notNullValue(String.class))));
    }
}