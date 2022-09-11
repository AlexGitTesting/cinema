package com.example.cinema.dao;

import com.example.cinema.domain.Movie_;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.domain.TimeTable_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeTableSpecificationImpl implements TimeTableSpecification {
    @Override
    public Specification<TimeTable> getByFilter(TimeTableQueryFilter filter) {
        return ((root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            query.orderBy(cb.asc(root.get(TimeTable_.movie).get(Movie_.id)));
            filter.getMovieId().ifPresent(id -> predicates.add(cb.equal(root.get(TimeTable_.movie).get(Movie_.id), id)));
            filter.getDateSession().ifPresentOrElse(localDate -> {
                        final LocalDateTime before = localDate.isEqual(LocalDate.now())
                                ? LocalDateTime.now()
                                : localDate.atTime(0, 0, 1);

                        final LocalDateTime after = localDate.atTime(23, 59, 59);
                        predicates.add(cb.between(root.get(TimeTable_.startSession), before, after));
                    },
                    () -> predicates.add(cb.greaterThan(root.get(TimeTable_.startSession), LocalDateTime.now())));
            return predicates.isEmpty()
                    ? null
                    : cb.and(predicates.toArray(Predicate[]::new));
        });
    }
}
