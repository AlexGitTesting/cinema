package com.example.cinema.dto;

import com.example.cinema.core.DtoMarker;
import com.example.cinema.domain.TimeTable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * Represent DTO of {@link TimeTable} to expose state for user.
 *
 * @author Alexandr Yefremov
 */
public record TimeTableDto(Long id, MovieDto movie, CinemaHallDto cinemaHall, LocalDateTime startSession,
                           Short basePrice, HashSet<Short> closedSeats,
                           Boolean isSold) implements Serializable, DtoMarker {
}
