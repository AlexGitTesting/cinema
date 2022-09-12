package com.example.cinema.dao;

import com.example.cinema.domain.Movie;
import com.example.cinema.domain.Movie_;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.domain.TimeTable_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Component
public class MovieSpecificationImpl implements MovieSpecification {
    @Override
    public Specification<Movie> getByFilter(@NonNull MovieQueryFilter filter) {
        return (((root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            applyOrder(root, query, cb, filter.isSortingAscending());
            filter.getTitle().ifPresent(title -> addPredicateIfExists(predicates, title, root.get(Movie_.title), cb));
            filter.getProducer().ifPresent((producer -> addPredicateIfExists(predicates, producer, root.get(Movie_.producer), cb)));
            if (filter.isActive()) {
                predicates.add(cb.in(root.get(Movie_.id)).value(findActiveFilmsFromTimeTable(cb, query)));
            }
            return predicates.isEmpty()
                    ? null
                    : cb.and(predicates.toArray(Predicate[]::new));
        }));
    }

    private Subquery<Long> findActiveFilmsFromTimeTable(CriteriaBuilder cb, CriteriaQuery<?> query) {
        final Subquery<Long> subQuery = query.subquery(Long.class);
        final Root<TimeTable> root = subQuery.from(TimeTable.class);
        final Path<LocalDateTime> path = root.get(TimeTable_.startSession);
        return subQuery.select(root.get(TimeTable_.movie).get(Movie_.id)).where(cb.greaterThan(path, LocalDateTime.now())).distinct(true);
    }

    private void addPredicateIfExists(final List<Predicate> predicates, final String field, Path<String> path, final CriteriaBuilder cb) {
        if (hasText(field)) predicates.add(cb.like(cb.lower(path), "%" + field.strip().toLowerCase() + "%"));
    }

    private void applyOrder(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder cb, boolean isSortingAscending) {
        if (isSortingAscending) {
            query.orderBy(
                    cb.asc(root.get(Movie_.title))
            );
        } else {
            query.orderBy(
                    cb.desc(root.get(Movie_.title)));
        }
    }

}
