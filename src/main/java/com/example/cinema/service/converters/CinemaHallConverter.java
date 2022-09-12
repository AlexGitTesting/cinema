package com.example.cinema.service.converters;

import com.example.cinema.domain.CinemaHall;
import com.example.cinema.dto.CinemaHallDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaHallConverter {
    CinemaHallDto toDto(CinemaHall source);
}
