package com.example.cinema.dao;

import com.example.cinema.domain.TimeTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long>, JpaSpecificationExecutor<TimeTable> {


    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t from #{#entityName} as t where t.movie.id=:movieId and t.isSold=false order by t.startSession asc ")
    Set<TimeTable> getTimeTableBriefByMovieId(@Param("movieId") @NonNull Long movieId);


    @EntityGraph(attributePaths = {"movie", "cinemaHall"}, type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t from #{#entityName} as t where t.movie.id=:movieId and t.isSold=false order by t.startSession asc ")
    Set<TimeTable> getTimeTableFullByMovieId(@Param("movieId") @NonNull Long movieId);

    @EntityGraph(attributePaths = {"movie", "cinemaHall"}, type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t from #{#entityName} as t where t.id=:id")
    Optional<TimeTable> getTimeTableByIdEager(@Param("id") @NonNull Long id);

    @EntityGraph(attributePaths = {"cinemaHall"}, type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t from #{#entityName} as t where t.id=:tableId ")
    Optional<TimeTable> getTimeTableByIdAndCinemaHallOnlyEager(@NonNull Long tableId);

    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select case when count(t.id)>0 then true else false end from" +
            " #{#entityName} as t where t.startSession<current_timestamp and t.cinemaHall.id=:id")
    boolean ifTimeTableExistsByCinemaHallIdLegacy(long id);

    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t.id from  #{#entityName} as t where t.cinemaHall.id=:id")
    Set<Long> findByCinemaHallId(Long id);

    @Query("delete from #{#entityName} as t where t.cinemaHall.id in:ids")
    void deleteByCinemaHallIds(Set<Long> ids);

    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select case when count(t.id)>0 then true else false end from" +
            " #{#entityName} as t where t.startSession>current_timestamp and t.cinemaHall.id=:id")
    boolean ifTimeTableExistsByCinemaHallIdFuture(Long id);

    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select case when count(t.id)>0 then true else false end from" +
            " #{#entityName} as t where t.startSession>current_timestamp and t.movie.id=:id")
    boolean ifTimeTableExistsByMovieIdInFuture(Long id);
}
