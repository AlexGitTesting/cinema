package com.example.cinema.dao;

import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.Movie;
import com.example.cinema.domain.TimeTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

/**
 * Repository to wprk with timetable entity.
 *
 * @author Alexandr Yefremov
 */
@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long>, JpaSpecificationExecutor<TimeTable> {
    /**
     * Gets timetable by id with cinema hall as eager fetching
     *
     * @param id timetable's id
     * @return optional of timetable
     */
    @EntityGraph(attributePaths = {"cinemaHall"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select t from #{#entityName} as t where t.id=:id")
    Optional<TimeTable> getTimeTableByIdEagerCinemaHallOnly(@Param("id") @NonNull Long id);

    /**
     * Gets timetable and overrides lazy fetching (movie and cinema hall) to eager.
     *
     * @param id timetable's id
     * @return Optional of timetable
     */
    @EntityGraph(attributePaths = {"movie", "cinemaHall"}, type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t from #{#entityName} as t where t.id=:id")
    Optional<TimeTable> getTimeTableByIdEager(@Param("id") @NonNull Long id);

    /**
     * Checks if timetable exists by cinema hall id and session starts after current moment
     *
     * @param id {@link CinemaHall#getId()}
     * @return true if exists, otherwise false
     */
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select case when count(t.id)>0 then true else false end from" +
            " #{#entityName} as t where t.startSession>current_timestamp and t.cinemaHall.id=:id")
    boolean ifTimeTableExistsByCinemaHallIdInFuture(Long id);

    /**
     * Checks if timetable exists by movie id and session starts after current moment
     *
     * @param id {@link Movie#getId()}
     * @return true if exists, otherwise false
     */
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select case when count(t.id)>0 then true else false end from" +
            " #{#entityName} as t where t.startSession>current_timestamp and t.movie.id=:id")
    boolean ifTimeTableExistsByMovieIdInFuture(Long id);
}
