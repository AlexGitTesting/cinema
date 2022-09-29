package com.example.cinema.service;

import com.example.cinema.core.ValidatorHelper;
import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.domain.CinemaHall;
import com.example.cinema.domain.TimeTable;
import com.example.cinema.dto.BasisTimeTable;
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
public interface TimeTableService {
    /**
     * Creates new time table from dto.
     *
     * @param dto {@link BasisTimeTable}
     * @return created timetable as {@link TimeTableDto}
     * @throws IllegalArgumentException if {@link BasisTimeTable#startSession()} is before then current date and time
     * @throws EntityNotFoundException  if movie is not exists by {@link BasisTimeTable#movieId()} or
     *                                  cinema hall is not exists by {@link BasisTimeTable#cinemaHallId()}
     */
    TimeTableDto createNew(BasisTimeTable dto) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Gets timetable by id as eager fetching.
     *
     * @param id timetable's id
     * @return {@link TimeTableDto}
     * @throws IllegalArgumentException if id not valid {@link ValidatorHelper#validateLong(Long)}
     * @throws EntityNotFoundException  if timetable not found by id
     */
    TimeTableDto getByIdEagerAsDto(Long id) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Gets timetable by id as eager fetching.
     *
     * @param id timetable's id
     * @return {@link TimeTableDto}
     * @throws IllegalArgumentException if id not valid {@link ValidatorHelper#validateLong(Long)}
     * @throws EntityNotFoundException  if timetable not found by id
     */
    TimeTable getByIdEager(Long id) throws IllegalArgumentException, EntityNotFoundException;

    /**
     * Gets paginated set of timetables by filter
     *
     * @param filter {@link TimeTableQueryFilter}
     * @return Page of TimeTableDto
     */
    Page<TimeTableDto> getByFiler(@NonNull TimeTableQueryFilter filter);

    /**
     * Updates existed timetable.
     *
     * @param table timetable
     * @return updated timetable
     */
    TimeTable updateTimeTable(TimeTable table);

    /**
     * Checks if timetable exists by cinema hall id in the future
     *
     * @param id {@link CinemaHall#getId()}
     * @return true if exists, otherwise false
     */
    boolean ifTimeTableExistsByCinemaHallIdInFuture(Long id);

    /**
     * Gets timetable as Optional with lazy linked entities
     *
     * @param id timetable id
     * @return Optional of TimeTable
     */
    Optional<TimeTable> getByIdOptionalLazy(Long id);
}
