package com.example.cinema.web;

import com.example.cinema.config.ProjectProperties;
import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.dto.MovieDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
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
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProjectProperties prop;

    @Nested
    class CreateMovie {
        @Test
        void create() throws Exception {
            final MovieDto dto = MovieDto.builder()
                    .producer("Petro")
                    .title("Go to California")
                    .timing((short) 15)
                    .build();
            final MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(prop.getBase().getMovie() + prop.getCreate())
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto));
            mockMvc.perform(post).andDo(print()).andExpect(status().isCreated());

        }

        @Test
        void createNotValidDto() throws Exception {
            final MovieDto dto = MovieDto.builder()
                    .producer("  ")//here
                    .title("Go to California")
                    .timing((short) 15)
                    .build();
            final MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(prop.getBase().getMovie() + "" + prop.getCreate())
                    .locale(new Locale("uk"))
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto));
            mockMvc.perform(post).andDo(print()).andExpect(status().isBadRequest());

        }
    }

    @Nested
    class GetById {
        @Test
        void getById() throws Exception {
            final MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(prop.getBase().getMovie() + prop.getGetById(), 1004)
                    .locale(new Locale("uk"))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE);
            mockMvc.perform(get)
                    .andDo(print())
                    .andExpect(status().isOk());

        }

        @Test
        void getByIdNotValidParam() throws Exception {
            final MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(prop.getBase().getMovie() + "" + prop.getGetById(), -2)
                    .locale(new Locale("uk"))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE);
            mockMvc.perform(get)
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }
    }

    @Nested
    class Update {
        @Test
        void update() throws Exception {
            final MovieDto dto = MovieDto.builder()
                    .producer("New name")
                    .title("Go to California")
                    .timing((short) 15)
                    .id(1005L)
                    .build();
            final MockHttpServletRequestBuilder patch = MockMvcRequestBuilders.patch(prop.getBase().getMovie() + "" + prop.getUpdate())
                    .content(objectMapper.writeValueAsString(dto))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE);
            mockMvc.perform(patch).andDo(print()).andExpect(status().isOk());
        }

        @Test
        void updateNotValid() throws Exception {
            final MovieDto dto = MovieDto.builder()
                    .producer("   ")
                    .title("Go to California")
                    .timing((short) 15)
                    .id(1005L)
                    .build();
            final MockHttpServletRequestBuilder patch = MockMvcRequestBuilders.patch(prop.getBase().getMovie() + "" + prop.getUpdate())
                    .content(objectMapper.writeValueAsString(dto))
                    .locale(new Locale("uk"))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE);
            mockMvc.perform(patch).andDo(print()).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class getByFilter {
        @Test
        void getByFilter() throws Exception {
            final MovieQueryFilter of = MovieQueryFilter.builder().limit(10).page(0).title("of").build();
            final MockHttpServletRequestBuilder accept = MockMvcRequestBuilders.post(prop.getBase().getMovie() + "" + prop.getGetByFilter())
                    .content(objectMapper.writeValueAsString(of))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE);
            mockMvc.perform(accept).andDo(print()).andExpect(status().isOk());
        }

        @Test
        void getByFilterByProducer() throws Exception {
            final MovieQueryFilter of = MovieQueryFilter.builder().limit(10).page(0).producer("Nel").build();
            final MockHttpServletRequestBuilder accept = MockMvcRequestBuilders.post(prop.getBase().getMovie() + "" + prop.getGetByFilter())
                    .content(objectMapper.writeValueAsString(of))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE);
            mockMvc.perform(accept).andDo(print()).andExpect(status().isOk());
        }

        @Test
        void getByFilterIsActive() throws Exception {
            final MovieQueryFilter of = MovieQueryFilter.builder().limit(30).page(0).sortingAscending(true).active(Boolean.TRUE).build();
            final MockHttpServletRequestBuilder accept = MockMvcRequestBuilders.post(prop.getBase().getMovie() + "" + prop.getGetByFilter())
                    .content(objectMapper.writeValueAsString(of))
                    .contentType(APPLICATION_JSON_VALUE)
                    .accept(APPLICATION_JSON_VALUE);
            mockMvc.perform(accept).andDo(print()).andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[*].movie.id", everyItem(is(1001))))
                    .andExpect(jsonPath("$.content[*].id", not(hasItem(6))));
        }
    }

    @Nested
    class DeleteMovie {
        @Test
        void delete() throws Exception {
            final MockHttpServletRequestBuilder biuld = MockMvcRequestBuilders.delete(prop.getBase().getMovie() + "" + prop.getDelete(), 1005);
            mockMvc.perform(biuld).andDo(print()).andExpect(status().isOk());
        }

        @Test
        void deleteNotFound() throws Exception {
            final MockHttpServletRequestBuilder biuld = MockMvcRequestBuilders.delete(prop.getBase().getMovie() + "" + prop.getDelete(), 1500);
            mockMvc.perform(biuld).andDo(print()).andExpect(status().isNotFound());
        }
    }
}