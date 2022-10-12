package com.example.cinema.web;

import com.example.cinema.config.ProjectProperties;
import com.example.cinema.core.ValidatorHelper;
import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.dto.BasisTimeTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiPredicate;

import static java.time.LocalDate.now;
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

    /**
     * Are two local dates equal.
     * The first argument is passed to the {@link DateMatcher#matches(Object)} under the hood.
     * The second argument is passed to {@link DateMatcher#getInstance(LocalDate)} .
     */
    private static class DateMatcher extends BaseMatcher<Object> {
        private final LocalDate date;

        private DateMatcher(LocalDate date) {
            this.date = date;
        }

        public static DateMatcher getInstance(LocalDate expectedDate) {
            return new DateMatcher(expectedDate);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(String.format(""" 
                            date and time of the start of the session and must match ( %s).
                            Expected format for parsing is {%s}
                             """
                    , date
                    , "yyyy-MM-ddTHH:mm:ss or as example {2011-12-03T10:15:30}"));
        }

        @Override
        public boolean matches(Object actual) {
            final LocalDateTime parse = getDateTime((CharSequence) actual);
            return parse.toLocalDate().equals(date);
        }

        @NotNull
        private LocalDateTime getDateTime(CharSequence actual) {
            return LocalDateTime.parse(actual, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            description.appendText(String.format("is %s and its local date is %s", item, getDateTime((CharSequence) item).toLocalDate()));
        }
    }

    /**
     * Applies BiPredicate interface logic to two dates.
     * The first argument is passed to the {@link DateMatcherFunction#matches(Object)} under the hood.
     * The second argument is passed to {@link DateMatcherFunction#getInstance(LocalDate, BiPredicate)} .
     */
    private static class DateMatcherFunction extends BaseMatcher<Object> {
        private final LocalDate date;
        private final BiPredicate<LocalDate, LocalDate> func;

        private DateMatcherFunction(LocalDate date, BiPredicate<LocalDate, LocalDate> func) {
            this.date = date;
            this.func = func;
        }

        /**
         * Apply function to compare to dates where dateForComparing is a second argument.
         *
         * @param dateForComparing second argument which is used in the function
         * @param func             function will be applied
         * @return instance uf the matcher
         */
        public static DateMatcherFunction getInstance(LocalDate dateForComparing, BiPredicate<LocalDate, LocalDate> func) {
            return new DateMatcherFunction(dateForComparing, func);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(String.format(""" 
                            date and time of the start of the session and must match the logic of comparing
                             described by BiPredicate's method. Where first argument is the item and second - argument of getInstance() method .
                            Expected format for parsing is {%s}, parsed and reformatting Item's value is %s
                             """
                    , "yyyy-MM-ddTHH:mm:ss or as example {2011-12-03T10:15:30}"
                    , date));
        }

        @Override
        public boolean matches(Object actual) {
            final LocalDateTime parse = getDateTime((CharSequence) actual);
            return func.test(parse.toLocalDate(), date);
        }

        @NotNull
        private LocalDateTime getDateTime(CharSequence actual) {
            return LocalDateTime.parse(actual, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            description.appendText(String.format("is %s and its local date is %s, argument for comparing is %s"
                    , item
                    , getDateTime((CharSequence) item).toLocalDate()
                    , date));
        }
    }

    @Nested
    @DisplayName("Create order")
    class CreateTimeTable {
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
        void createNotValidDto1() throws Exception {
            final LocalDateTime date = LocalDateTime.now().plusDays(3);
            final BasisTimeTable table = new BasisTimeTable(-3L, null
                    , null
                    , (short) -35);
            final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getCreate()).accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(table));
            final String content = mockMvc.perform(post)
                    .andDo(print())
                    .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
            assertTrue(content.toLowerCase().contains("Validation exception".toLowerCase())
                    && content.toLowerCase().contains("movieId".toLowerCase())
                    && content.toLowerCase().contains("cinemaHallId".toLowerCase())
                    && content.toLowerCase().contains("startSession".toLowerCase())
                    && content.toLowerCase().contains("basePrice".toLowerCase())
            );
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
        void createMovieNotFound() throws Exception {
            final LocalDateTime date = LocalDateTime.now().plusDays(3);
            final BasisTimeTable table = new BasisTimeTable(1500L, 100L
                    , date, (short) 35);
            final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getCreate()).accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(table));
            mockMvc.perform(post)
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        void createCinemaHAllNotFound() throws Exception {
            final LocalDateTime date = LocalDateTime.now().plusDays(3);
            final BasisTimeTable table = new BasisTimeTable(1000L, 1000L
                    , date, (short) 35);
            final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getCreate()).accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(table));
            mockMvc.perform(post)
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Get by id cases")
    class GetById {
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
    }

    @Nested
    @DisplayName(" Get by filter cases")
    class GetByFilter {
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
                    .andExpect(jsonPath("$.content[*].cinemaHall.name", everyItem(notNullValue(String.class))))
                    .andExpect(jsonPath("$.content[*].startSession",
                            everyItem(DateMatcherFunction.getInstance(now(), (item, second) ->
                                    item.isEqual(second) || item.isAfter(second)
                            ))));
        }

        @Test
        void getByFilter1() throws Exception {
            final LocalDate now = now();
            final TimeTableQueryFilter filter = TimeTableQueryFilter.builder().dateSession(now).build();
            final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getGetByFilter())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(filter));
            mockMvc.perform(post)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[*].startSession", everyItem(notNullValue(String.class))))
                    .andExpect(jsonPath("$.content[*].startSession", everyItem(DateMatcher.getInstance(now))));
        }

        @Test
        void getByFilterActiveFilmsOnly() throws Exception {
            final TimeTableQueryFilter filter = TimeTableQueryFilter.builder().hasFreeSeats(Boolean.TRUE).build();
            final MockHttpServletRequestBuilder post = post(prop.getBase().getTimeTable() + prop.getGetByFilter())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(filter));
            mockMvc.perform(post)
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[*].isSold", everyItem(equalTo(Boolean.FALSE))))
            ;
        }
    }
}