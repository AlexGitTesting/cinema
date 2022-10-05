package com.example.cinema.service;

import com.example.cinema.core.CustomConstraintException;
import com.example.cinema.dto.CinemaHallDto;

import javax.persistence.EntityNotFoundException;

/**
 * Service for Cinema hall.
 *
 * @author Alexandr Yefremov
 */
public interface CinemaHallService {

    /**
     * Creates new cinema hall.
     *
     * @param dto {@link CinemaHallDto}
     * @return saved {@link CinemaHallDto}
     * @throws CustomConstraintException if {@link CinemaHallDto#name()} contains not unique name
     */
    CinemaHallDto createCinemaHall(CinemaHallDto dto) throws CustomConstraintException;

    /**
     * Retrieves cinema hall by id.
     *
     * @param id cinema hall's id
     * @return found cinema hall
     * @throws EntityNotFoundException if cinema hall by current id not found
     */
    CinemaHallDto readById(Long id) throws EntityNotFoundException;

    /**
     * Updates existed cinema hall.
     *
     * @param dto {@link CinemaHallDto}
     * @return updated cinema hall
     * @throws IllegalArgumentException  if there will sessions in this cinema hall in the future
     * @throws EntityNotFoundException   if cinema hall is not existed by {@link CinemaHallDto#id()}
     * @throws CustomConstraintException if {@link CinemaHallDto#name()} contains not unique name
     */
    CinemaHallDto update(CinemaHallDto dto) throws IllegalArgumentException, EntityNotFoundException, CustomConstraintException;

    /**
     * Removes cinema hall by id.
     *
     * @param id {@link CinemaHallDto#id()}
     * @return id
     * @throws IllegalArgumentException if you try remove cinema hall, which will be used for future sessions
     */
    Long delete(Long id) throws IllegalArgumentException;

}
