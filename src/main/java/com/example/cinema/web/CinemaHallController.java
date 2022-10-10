package com.example.cinema.web;

import com.example.cinema.core.CreateUpdateContract;
import com.example.cinema.core.DeleteContract;
import com.example.cinema.core.GetByIdContract;
import com.example.cinema.dto.CinemaHallDto;
import com.example.cinema.dto.MovieDto;
import com.example.cinema.service.CinemaHallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.cinema.core.ValidatorHelper.validateParam;

@RestController
@RequestMapping(path = "${url.base.cinema_hall}")
@Tag(name = "Cinema hall controller.", description = "Handles HTTP requests connected with Cinema hall")
public class CinemaHallController implements CreateUpdateContract<CinemaHallDto>, DeleteContract, GetByIdContract<CinemaHallDto> {
    private final CinemaHallService service;
    private final ResponseExceptionHandler exceptionHandler;

    public CinemaHallController(CinemaHallService service, ResponseExceptionHandler exceptionHandler) {
        this.service = service;
        this.exceptionHandler = exceptionHandler;
    }

    @Operation(summary = "Creates new cinema hall.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cinema hall is created", content = @Content(schema = @Schema(implementation = CinemaHallDto.class))),
            @ApiResponse(responseCode = "400", description = "Not valid dto"
                    , content = @Content(schema = @Schema(implementation = String.class)
                    , examples = {@ExampleObject(name = "Validation exception message", value = """
                    Validation exception:
                    Field: name,  message: Field must contain at least one non-whitespace symbol,
                    Field: id,  message: The field must be null,
                    Field: seatsAmount,  message: The minimal value must be 1,
                    Field: seatsType,  message: Collection must be not empty,"""),
                    @ExampleObject(name = "ConstraintViolationException", value = "Cinema hall with name {?} already exists, but must be unique")}))
    })
    @Override
    public CinemaHallDto create(@RequestBody(description = "Cinema hall dto for creation",
            content = @Content(examples = {
                    @ExampleObject(name = "Not valid id", value = "{\"id\":2,\"name\":\"name\",\"seatsAmount\":10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
                    @ExampleObject(name = "Not valid name", value = "{\"id\":null,\"name\":\"    \",\"seatsAmount\":10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
                    @ExampleObject(name = "Not valid seats amount", value = "{\"id\":null,\"name\":\"name\",\"seatsAmount\":-10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
                    @ExampleObject(name = "Empty seats types", value = "{\"id\":null,\"name\":\"name\",\"seatsAmount\":10,\"seatsType\":{}}"),
                    @ExampleObject(name = "Cinema hall with such name has already existed", value = "{\"id\":null,\"name\":\"RED\",\"seatsAmount\":10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
            })) CinemaHallDto dto) {
        return service.createCinemaHall(dto);
    }

    @Operation(summary = "Updates existed cinema hall.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema hall is updated", content = @Content(schema = @Schema(implementation = CinemaHallDto.class))),
            @ApiResponse(responseCode = "400", description = "Not valid dto"
                    , content = @Content(schema = @Schema(implementation = String.class)
                    , examples = {
                    @ExampleObject(name = "Validation exception message", value = """
                            Validation exception:
                            Field: name,  message: Field must contain at least one non-whitespace symbol,
                            Field: id,  message: The field must be not null,
                            Field: seatsAmount,  message: The minimal value must be 1,
                            Field: seatsType,  message: Collection must be not empty,"""),
                    @ExampleObject(name = "There are sessions in the future", value = "You can not update cinema hall, because there will sessions in this cinema hall in the future"),
                    @ExampleObject(name = "ConstraintViolationException", value = "Cinema hall with name {?} already exists, but must be unique")
            })),
            @ApiResponse(responseCode = "404", description = "Cinema hall not found by id", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @Override
    public CinemaHallDto update(@RequestBody(description = "Cinema hall dto for creation",
            content = @Content(examples = {
                    @ExampleObject(name = "Not valid id", value = "{\"id\":null,\"name\":\"name\",\"seatsAmount\":10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
                    @ExampleObject(name = "Not valid name", value = "{\"id\":null,\"name\":\"    \",\"seatsAmount\":10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
                    @ExampleObject(name = "Not valid seats amount", value = "{\"id\":null,\"name\":\"name\",\"seatsAmount\":-10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
                    @ExampleObject(name = "Empty seats types", value = "{\"id\":null,\"name\":\"name\",\"seatsAmount\":10,\"seatsType\":{}}"),
                    @ExampleObject(name = "Cinema hall with such name has already existed", value = "{\"id\":null,\"name\":\"RED\",\"seatsAmount\":10,\"seatsType\":{\"BLIND\":[1,2,3,4,5],\"LUXURY\":[6,7],\"KISSES\":[8,9,10]}}"),
            })) CinemaHallDto dto) {
        return service.update(dto);
    }

    @Operation(summary = "Removes existed Cinema hall by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "Cinema hall with id ? successfully removed."))),
            @ApiResponse(responseCode = "400", headers = @Header(name = "Error message",
                    schema = @Schema(implementation = String.class), description = "Argument is invalid. Number must be not null and greater then 0")),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(value = "Cinema hall is not found by id")))
    })
    @Override
    public ResponseEntity<String> delete(@Parameter(description = "Cinema hall id. Not null and greater then 0", examples = {
            @ExampleObject(name = "Valid id", value = "6"),
            @ExampleObject(name = "Invalid id,less 0", value = "-5"),
            @ExampleObject(name = "Invalid id, null", value = "null"),
            @ExampleObject(name = "Not found", value = "1500")
    }) Long id) {
        validateParam(id);
        final Long delete = service.delete(id);
        assert id.equals(delete);
        final String message = exceptionHandler.prepareLocalizedMessage("cinema.hall.removed"
                , String.format("Cinema hall with id %d successfully removed.", id)
                , new Object[]{id});
        return ResponseEntity.ok().body(message);
    }

    @Operation(summary = "Retrieves cinema hall.", description = "Retrieves cinema hall by id. Existed values from 1 to 6")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cinema hall is found", content = @Content(schema = @Schema(implementation = MovieDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter",
                    headers = @Header(name = "Error message", schema = @Schema(implementation = String.class),
                            description = "Argument is invalid. Number must be not null and greater then 0")),
            @ApiResponse(responseCode = "404", description = "Cinema hall by id not found",
                    content = @Content(schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "Cinema hall by id not found")))
    })
    @Override
    public CinemaHallDto getById(@Parameter(description = "Cinema hall id. Not null and greater then 0", examples = {
            @ExampleObject(name = "Valid id", value = "6"),
            @ExampleObject(name = "Invalid id,less 0", value = "-5"),
            @ExampleObject(name = "Invalid id, null", value = "null"),
            @ExampleObject(name = "Not found", value = "1500")
    }) Long id) {
        validateParam(id);
        return service.readById(id);
    }
}
