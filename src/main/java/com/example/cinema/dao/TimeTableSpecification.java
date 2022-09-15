package com.example.cinema.dao;

import com.example.cinema.domain.TimeTable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

/**
 * Specification to work with {@link TimeTable}.
 *
 * @author Alexandr Yefremov
 */
public interface TimeTableSpecification {
    Specification<TimeTable> getByFilter(@NonNull TimeTableQueryFilter filter);
}
