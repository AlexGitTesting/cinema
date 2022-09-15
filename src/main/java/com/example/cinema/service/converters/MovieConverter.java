package com.example.cinema.service.converters;

import com.example.cinema.domain.Movie;
import com.example.cinema.dto.MovieDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Movie converter.
 *
 * @author Alexandr Yefremov
 */
@Mapper(componentModel = "spring")
public interface MovieConverter {
    // TODO: 15.09.2022 ignore auditable
    MovieDto toDto(Movie source);

    @Mapping(target = "id", expression = "java(source.getId().orElse(null))")
    @Mapping(target = "title", expression = "java(source.getTitle().orElse(null))")
    @Mapping(target = "timing", expression = "java(source.getTiming().orElse(null))")
    @Mapping(target = "producer", expression = "java(source.getProducer().orElse(null))")
    Movie toDomain(MovieDto source);


}
