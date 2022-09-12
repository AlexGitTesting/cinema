package com.example.cinema.dao;

import com.example.cinema.domain.Movie;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public interface MovieSpecification {
    Specification<Movie> getByFilter(@NonNull MovieQueryFilter filter);
}
