package com.example.cinema.web;

import com.example.cinema.core.FilterableContract;
import com.example.cinema.core.GetByIdContract;
import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.dto.BasisTimeTable;
import com.example.cinema.dto.TimeTableDto;
import com.example.cinema.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.cinema.core.ValidatorHelper.validateParam;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Handles HTTP requests connected with Timetable.
 *
 * @author Alexandr Yefremov
 */
@RestController
@RequestMapping(path = "${url.base.time_table}")
@Tag(name = "Timetable controller", description = "Handles HTTP requests connected with Timetable")
public class TimetableController implements GetByIdContract<TimeTableDto>, FilterableContract<TimeTableDto, TimeTableQueryFilter> {

    private final TimeTableService service;

    public TimetableController(TimeTableService service) {
        this.service = service;
    }

    @Operation(summary = "Creates new timetable")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "${url.create}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    TimeTableDto create(@RequestBody BasisTimeTable dto) {
        return service.createNew(dto);
    }

    @Operation(summary = "Gets timetable", description = "Gets timetable by id")
    @Override
    public TimeTableDto getById(@Parameter(description = "Timetable id. Not null and greater then 0") Long id) {
        validateParam(id);
        return service.getByIdEagerAsDto(id);
    }

    @Operation(summary = "Searches timetable by filter")
    @Override
    public Page<TimeTableDto> getByFilter(TimeTableQueryFilter filter) {
        return service.getByFiler(filter);
    }
}
