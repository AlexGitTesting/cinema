package com.example.cinema.dao;

import com.example.cinema.domain.OrderTable;
import com.example.cinema.domain.TimeTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

/**
 * Repository for Orders.
 *
 * @author Alexandr Yefremov
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderTable, Long> {
    /**
     * Retrieves {@link OrderTable} by id with eager fetching of the {@link TimeTable}.
     *
     * @param id order's id
     * @return optional of {@link OrderTable}
     */
    @EntityGraph(attributePaths = {"timeTable"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select o from #{#entityName} as o where o.id=:id")
    Optional<OrderTable> findOrderByIdTimeTableEager(@NonNull Long id);


    /**
     * Retrieves {@link OrderTable} by id with eager fetching of the {@link TimeTable},
     * {@link com.example.cinema.domain.CinemaHall} and {@link com.example.cinema.domain.Movie}.
     *
     * @param id order's id
     * @return optional of {@link OrderTable}
     */
    @EntityGraph(attributePaths = {"timeTable", "timeTable.cinemaHall", "timeTable.movie"}, type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select o from #{#entityName} as o where o.id=:id")
    Optional<OrderTable> findOrderByIdEagerAllAttributes(@NonNull Long id);


}
