package com.example.cinema.web;

import com.example.cinema.config.ProjectProperties;
import com.example.cinema.dto.OrderDto;
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

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql({
        "classpath:statements/insert_cinema_hall.sql",
        "classpath:statements/insert_movie.sql",
        "classpath:statements/insert_time_table.sql",
        "classpath:statements/insert_order_table.sql"
})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
        "classpath:statements/truncate_time_table.sql",
        "classpath:statements/truncate_movie.sql",
        "classpath:statements/truncate_cinema_hall.sql",
        "classpath:statements/truncate_order_table.sql"
})
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProjectProperties prop;

    @Nested
    class CreateOrder {
        @Test
        void createOrder() throws Exception {
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(null)
                    .seats(Set.of((short) 1, (short) 5, (short) 4))
                    .timeTableId(1029L)
                    .orderPrice(null)
                    .build();
            final MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(prop.getBase().getOrder() + prop.getCreate())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(order))
                    .accept(APPLICATION_JSON);
            mockMvc.perform(post).andDo(print()).andExpect(status().isCreated());
        }

        @Test
        void createOrderNotValidDto() throws Exception {
            final OrderDto order = OrderDto.builder()
                    .customer("customer")
                    .id(1L)
                    .seats(Collections.emptySet())
                    .timeTableId(null)
                    .orderPrice(20)
                    .customer("  ")
                    .build();
            final MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(prop.getBase().getOrder() + prop.getCreate())
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(order))
                    .accept(APPLICATION_JSON);
            mockMvc.perform(post).andDo(print()).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class OrderGetById {
        @Test
        void getById() throws Exception {
            final MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(prop.getBase().getOrder() + prop.getGetById(), 147)
                    .locale(new Locale("uk"));
            mockMvc.perform(get).andDo(print()).andExpect(status().isOk());
        }

        @Test
        void getByIdNotValidParam() throws Exception {
            final MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(prop.getBase().getOrder() + prop.getGetById(), -37)
                    .locale(new Locale("uk"));
            mockMvc.perform(get).andDo(print()).andExpect(status().isBadRequest());
        }
    }

    @Test
    void delete() throws Exception {
        final MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete(prop.getBase().getOrder() + prop.getDelete(), 147);
        mockMvc.perform(delete).andDo(print()).andExpect(status().isOk());
    }
}