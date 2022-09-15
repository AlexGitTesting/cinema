package com.example.cinema.dto;

import com.example.cinema.core.SeatType;

import java.util.EnumMap;
import java.util.HashSet;

public record CinemaHallDto(Long id, String name, Short seatsAmount, EnumMap<SeatType, HashSet<Short>> seatsType) {
}
