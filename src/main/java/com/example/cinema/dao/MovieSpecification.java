package com.example.cinema.dao;

import com.example.cinema.domain.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

/**
 * Specification for {@link Movie}.
 *
 * @author Alexandr Yefremov
 */
public interface MovieSpecification {
    Specification<Movie> getByFilter(@NonNull MovieQueryFilter filter);
}
