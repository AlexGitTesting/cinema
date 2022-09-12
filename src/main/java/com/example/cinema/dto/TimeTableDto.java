package com.example.cinema.dto;

import java.time.LocalDateTime;
import java.util.HashSet;

public record TimeTableDto(Long id, MovieDto movie, CinemaHallDto cinemaHall, LocalDateTime startSession,
                           Short basePrice, HashSet<Short> closedSeats, Boolean isSold) {
}
