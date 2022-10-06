package com.example.cinema.dao;

import com.example.cinema.domain.OrderTable;
import com.example.cinema.domain.TimeTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

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
class OrderRepositoryTest extends BaseDataJpaTest {
    @Autowired
    private OrderRepository repository;

    @Test
    void create() {
        final TimeTable timeTable = new TimeTable();
        timeTable.setId(1000L);
        final OrderTable orderTable = new OrderTable(null, timeTable, 555, "I'm");
        assertDoesNotThrow(() -> repository.saveAndFlush(orderTable));
    }

    @Test
    void findOrderTableById() {
        final OrderTable orderTable = assertDoesNotThrow(() -> repository.findOrderByIdTimeTableEager(106L).orElseThrow());
        assertEquals(1004L, orderTable.getTimeTable().getId());
    }

    @Test
    void deleteOrder() {
        final long id = 106L;
        assertDoesNotThrow(() -> repository.deleteById(id));
        assertFalse(repository.findById(id).isPresent());
    }

}
