package com.example.cinema.dao;

import com.example.cinema.domain.TimeTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Set;

import static org.hibernate.jpa.QueryHints.HINT_READONLY;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long>, JpaSpecificationExecutor<TimeTable> {


    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t from #{#entityName} as t where t.movie.id=:movieId and t.isSold=false order by t.startSession asc ")
    Set<TimeTable> getTimeTableBriefByMovieId(@Param("movieId") @NonNull Long movieId);


    @EntityGraph(attributePaths = {"movie, cinemaHall"}, type = EntityGraph.EntityGraphType.FETCH)
    @QueryHints(@QueryHint(name = HINT_READONLY, value = "true"))
    @Query("select t from #{#entityName} as t where t.movie.id=:movieId and t.isSold=false order by t.startSession asc ")
    Set<TimeTable> getTimeTableFullByMovieId(@Param("movieId") @NonNull Long movieId);
}
