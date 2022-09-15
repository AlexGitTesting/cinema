package com.example.cinema.service.converters;

import com.example.cinema.domain.CinemaHall;
import com.example.cinema.dto.CinemaHallDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CinemaHallConverter {
    CinemaHallDto toDto(CinemaHall source);

    CinemaHall toDomain(CinemaHallDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seatsType", expression = "java(source.seatsType())")
    void toDomainTarget(CinemaHallDto source, @MappingTarget CinemaHall target);
}
