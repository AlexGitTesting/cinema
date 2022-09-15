package com.example.cinema.service.converters;

import com.example.cinema.domain.Movie;
import com.example.cinema.dto.MovieDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Movie converter.
 *
 * @author Alexandr Yefremov
 */
@Mapper(componentModel = "spring")
public interface MovieConverter {
    MovieDto toDto(Movie source);

    @Mapping(target = "id", expression = "java(source.getId().orElse(null))")
    @Mapping(target = "title", expression = "java(source.getTitle().orElse(null))")
    @Mapping(target = "timing", expression = "java(source.getTiming().orElse(null))")
    @Mapping(target = "producer", expression = "java(source.getProducer().orElse(null))")
    Movie toDomain(MovieDto source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", expression = "java(source.getTitle().orElse(null))")
    @Mapping(target = "timing", expression = "java(source.getTiming().orElse(null))")
    @Mapping(target = "producer", expression = "java(source.getProducer().orElse(null))")
    void toDomain(MovieDto source, @MappingTarget Movie target);


}
