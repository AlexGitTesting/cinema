package com.example.cinema.dao;

import com.example.cinema.domain.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

/**
 * Repository for {@link CinemaHall}.
 *
 * @author Alexandr Yefremov
 */
@Repository
public interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {
    /**
     * Gets cinema hall by id and wraps it in optional.
     *
     * @param id cinema hall's id
     * @return Optional of cinema hall
     */
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select c from #{#entityName} as c where c.id=:id")
    Optional<CinemaHall> getCinemaHall(@Param("id") @NonNull Long id);
}
