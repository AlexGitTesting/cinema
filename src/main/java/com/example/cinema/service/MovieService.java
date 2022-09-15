package com.example.cinema.service;

import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.domain.Movie;
import com.example.cinema.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import javax.persistence.EntityNotFoundException;

/**
 * Service to work with {@link Movie}.
 *
 * @author Alexandr Yefremov
 */
public interface MovieService {

    /**
     * Gets paginated movie dto by filter.
     *
     * @param filter {@link MovieQueryFilter}
     * @return Page<MovieDto>
     * @throws NullPointerException if filter is null
     */
    Page<MovieDto> getByFiler(@NonNull MovieQueryFilter filter) throws NullPointerException;

    /**
     * Creates new Movie and return dto.
     *
     * @param dto {@link MovieDto}
     * @return {@link MovieDto}
     */
    MovieDto create(MovieDto dto);

    /**
     * Retrieves movie by id.
     *
     * @param id movie's id
     * @return {@link MovieDto}
     * @throws EntityNotFoundException if movie not fount by id
     */
    MovieDto getById(Long id) throws EntityNotFoundException;

    /**
     * Updates movie if it exists.
     *
     * @param dto {@link MovieDto}
     * @return updated movie
     * @throws EntityNotFoundException  if movie not fount
     * @throws IllegalArgumentException if dto does not contain id, it is incorrect
     */
    MovieDto update(MovieDto dto) throws EntityNotFoundException, IllegalArgumentException;

    /**
     * Removes movie by id, if there are not timetables that are referring to this one
     *
     * @param id movie id
     * @return true if all went good
     * @throws IllegalArgumentException if movie is still referenced
     */
    boolean delete(Long id) throws IllegalArgumentException;

}
