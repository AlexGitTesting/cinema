package com.example.cinema.dao;

import com.example.cinema.domain.TimeTable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public interface TimeTableSpecification {
    Specification<TimeTable> getByFilter(@NonNull TimeTableQueryFilter filter);
}
