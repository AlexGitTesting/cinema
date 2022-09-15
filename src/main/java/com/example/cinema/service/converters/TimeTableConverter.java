package com.example.cinema.service.converters;

import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.TimeTableDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

/**
 * Mapper for Timetable.
 *
 * @author Alexandr Yefremov
 */
@Mapper(componentModel = "spring", uses = {MovieConverter.class, CinemaHallConverter.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TimeTableConverter {
    TimeTableDto toDto(TimeTable source);


}
