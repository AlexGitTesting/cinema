package com.example.cinema.service;

import com.example.cinema.dto.CinemaHallDto;

import javax.persistence.EntityNotFoundException;

/**
 * Service for Cinema hall.
 *
 * @author Alexandr Yefremov
 */
public interface CinemaHallService {
    CinemaHallDto createCinemaHall(CinemaHallDto dto);

    CinemaHallDto readById(Long id) throws EntityNotFoundException;

    CinemaHallDto update(CinemaHallDto dto) throws IllegalArgumentException, EntityNotFoundException;

    Long delete(Long id) throws IllegalArgumentException, EntityNotFoundException;

}
