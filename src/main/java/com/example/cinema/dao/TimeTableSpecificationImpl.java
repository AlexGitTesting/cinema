package com.example.cinema.dao;

import com.example.cinema.domain.Movie_;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.domain.TimeTable_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link TimeTableSpecification}.
 *
 * @author Alexandr Yefremov
 */
@Component
public class TimeTableSpecificationImpl implements TimeTableSpecification {
    @Override
    public Specification<TimeTable> getByFilter(@NonNull TimeTableQueryFilter filter) {
        return ((root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            query.orderBy(cb.asc(root.get(TimeTable_.startSession)));
            if (!query.getResultType().equals(Long.class)) {
                root.fetch(TimeTable_.movie);
                root.fetch(TimeTable_.cinemaHall);
            }
            filter.getMovieId().ifPresent(id -> predicates.add(cb.equal(root.get(TimeTable_.movie).get(Movie_.id), id)));
            filter.getDateSession().ifPresentOrElse(localDate -> {
                        predicates.add(cb.between(
                                root.get(TimeTable_.startSession)
                                , localDate.atTime(0, 0, 1)
                                , localDate.atTime(23, 59, 59))
                        );
                    },
                    () -> predicates.add(cb.greaterThan(root.get(TimeTable_.startSession), LocalDateTime.now())));
            return predicates.isEmpty()
                    ? null
                    : cb.and(predicates.toArray(Predicate[]::new));
        });
    }
}
