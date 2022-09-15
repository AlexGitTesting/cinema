package com.example.cinema.dao;

import com.example.cinema.domain.OrderTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @EntityGraph(attributePaths = {"timeTable"}, type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    Optional<OrderTable> findOrderTableById(@NonNull Long id);
}
