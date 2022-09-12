package com.example.cinema.service.converters;

import com.example.cinema.domain.Movie;
import com.example.cinema.dto.MovieDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieConverter {
    MovieDto toDto(Movie source);

    @Mapping(target = "id", expression = "java(source.getId().orElse(null))")
    Movie toDomain(MovieDto source);


}
