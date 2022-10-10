package com.example.cinema.web;

import com.example.cinema.core.FilterableContract;
import com.example.cinema.core.GetByIdContract;
import com.example.cinema.dao.TimeTableQueryFilter;
import com.example.cinema.dto.BasisTimeTable;
import com.example.cinema.dto.TimeTableDto;
import com.example.cinema.service.TimeTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Timetable created", content = @Content(schema = @Schema(implementation = TimeTableDto.class))),
                    @ApiResponse(responseCode = "400", description = "Not valid dto or start session",
                            content = @Content(schema = @Schema(implementation = String.class),
                                    examples = {
                                            @ExampleObject(name = "Validation exception message", value = """
                                                    Validation exception:
                                                    Field: startSession,  message: The field must have a value,
                                                    Field: movieId,  message: The minimal value must be 1,
                                                    Field: cinemaHallId,  message: The field must have a value,
                                                    Field: basePrice,  message: The minimal value must be 0,"""),
                                            @ExampleObject(name = "Start session  can not be earlier then this moment",
                                                    value = "Start session  can not be earlier then this moment")})),
                    @ApiResponse(responseCode = "404", description = "Movie or Cinema hall by id not found",
                            content = @Content(schema = @Schema(implementation = String.class),
                                    examples = {
                                            @ExampleObject(value = "Movie by id not found", name = "Movie by id not found"),
                                            @ExampleObject(value = "Cinema hall not found by id", name = "Cinema hall not found by id")}))})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "${url.create}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    TimeTableDto create(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Timetable for creation",
            content = @Content(schema = @Schema(implementation = BasisTimeTable.class),
                    examples = {
                            @ExampleObject(name = "Not valid DTO", value = "{\"movieId\":-3,\"cinemaHallId\":null,\"startSession\":null,\"basePrice\":-35}"),
                            @ExampleObject(name = "Valid DTO", value = "{\"movieId\":1000,\"cinemaHallId\":100,\"startSession\":\"2022-10-13T22:49:40.8526179\",\"basePrice\":35}"),
                            @ExampleObject(name = "Movie by id not fount", value = "{\"movieId\":1500,\"cinemaHallId\":100,\"startSession\":\"2022-10-13T22:49:40.8526179\",\"basePrice\":35}"),
                            @ExampleObject(name = "Cinema hall by id not found", value = "{\"movieId\":1000,\"cinemaHallId\":1000,\"startSession\":\"2022-10-13T22:49:40.8526179\",\"basePrice\":35}"),
                            @ExampleObject(name = "Incorrect start session", value = "{\"movieId\":1000,\"cinemaHallId\":100,\"startSession\":\"2022-10-10T22:49:40.8526179\",\"basePrice\":35}")
                    })
    ) @RequestBody BasisTimeTable dto) {
        return service.createNew(dto);
    }

    @Operation(summary = "Gets timetable", description = "Gets timetable by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timetable is found", content = @Content(schema = @Schema(implementation = TimeTableDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter",
                    headers = @Header(name = "Error message", schema = @Schema(implementation = String.class),
                            description = "Argument is invalid. Number must be not null and greater then 0")),
            @ApiResponse(responseCode = "404", description = "Timetable by id not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Timetable by id not found")))
    })
    @Override
    public TimeTableDto getById(@Parameter(description = "Timetable id. Not null and greater then 0", examples = {
            @ExampleObject(name = "Valid id", value = "1"),
            @ExampleObject(name = "Invalid id,less then 0", value = "-5"),
            @ExampleObject(name = "Invalid id, null", value = "null"),
            @ExampleObject(name = "Not found", value = "2000")
    }) Long id) {
        validateParam(id);
        return service.getByIdEagerAsDto(id);
    }

    @ApiResponse(responseCode = "200", description = "Timetable is found", content = {@Content(schema = @Schema(implementation = TimeTableDto.class))})
    @Operation(summary = "Searches timetable by filter")
    @Override
    public Page<TimeTableDto> getByFilter(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = """
            Filter for searching time table. If date session is specified,
            then timetables for that day will be shown, otherwise timetables whose start session is after then current moment""",
            content = @Content(schema = @Schema(implementation = TimeTableQueryFilter.class),
                    examples = {
                            @ExampleObject(name = "By movie id", value = "{\"page\":0,\"limit\":2,\"dateSession\":null,\"movieId\":1}"),
                            @ExampleObject(name = "By date session", value = "{\"page\":0,\"limit\":2,\"dateSession\":\"2022-10-11\",\"movieId\":null}")
                    })) TimeTableQueryFilter filter) {
        return service.getByFiler(filter);
    }
}
