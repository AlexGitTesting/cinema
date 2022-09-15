package com.example.cinema.service;

import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.TimeTableDto;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * Represents contract for Timetable service.
 *
 * @author Alexandr Yefremov
 */
public interface TimeTableService { // TODO: 15.09.2022  ecplanation
    TimeTableDto getByIdEagerAsDto(Long id) throws IllegalArgumentException, EntityNotFoundException;

    TimeTable getByIdEager(Long id) throws IllegalArgumentException, EntityNotFoundException;

    TimeTable getByIdWithCinemaHallOnlyEager(Long id) throws IllegalArgumentException, EntityNotFoundException;// TODO: 15.09.2022 clean

    Page<TimeTableDto> getByFiler(@NonNull TimeTableQueryFilter filter);

    TimeTable updateTimeTable(TimeTable table);

    boolean ifTimeTableExistsByCinemaHallIdLegacy(Long id);

    boolean ifTimeTableExistsByCinemaHallIdFuture(Long id);

    Optional<TimeTable> getByIdOptionalLazy(Long id);
}
