package com.example.cinema.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(properties = {"url.create=_create",
        "url.get-by-id=_get.json",
        "url.update=_update",
        "url.get-by-filter=_filter",
        "url.delete=_delete",
        "url.base.order=_order",
        "url.base.movie=_movie",
        "url.base.cinema_hall=_cinema-hall",
        "url.base.time_table=_time_table"})
class ProjectPropertiesTest {
    @Autowired
    private ProjectProperties prop;

    @Test
    void getCreate() {
        assertEquals("_create", prop.getCreate());
    }

    @Test
    void getGetById() {
        assertEquals("_get.json", prop.getGetById());
    }

    @Test
    void getGetByFilter() {
        assertEquals("_filter", prop.getGetByFilter());
    }

    @Test
    void getUpdate() {
        assertEquals("_update", prop.getUpdate());
    }

    @Test
    void getDelete() {
        assertEquals("_delete", prop.getDelete());
    }

    @Test
    void getBase() {
        assertEquals("_order", prop.getBase().getOrder());
        assertEquals("_movie", prop.getBase().getMovie());
        assertEquals("_cinema-hall", prop.getBase().getCinemaHall());
        assertEquals("_time_table", prop.getBase().getTimeTable());
    }
}