package com.example.cinema.web;

import com.example.cinema.core.CreateUpdateContract;
import com.example.cinema.core.DeleteContract;
import com.example.cinema.core.FilterableContract;
import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.dto.MovieDto;
import com.example.cinema.service.MovieService;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.cinema.core.ValidatorHelper.validateParam;

/**
 * Handles HTTP requests connected with Movie.
 *
 * @author Alexandr Yefremov
 */
@RestController
@RequestMapping(path = "${url.base.movie}")
@Tag(name = "Movie controller", description = "Handles HTTP requests connected with Movie")
public class MovieController implements CreateUpdateContract<MovieDto>, DeleteContract, FilterableContract<MovieDto, MovieQueryFilter> {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @Operation(summary = "Creates new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movie is created", content = @Content(schema = @Schema(implementation = MovieDto.class))),
            @ApiResponse(responseCode = "400", description = "Not valid dto")
    })
    @Override
    public MovieDto create(@RequestBody(description = "Movie Dto for creation",
            content = @Content(examples = {@ExampleObject(name = "Valid dto", value = "{\"id\":null,\"title\":\"Go to California\",\"timing\":15,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid id", value = "{\"id\":1,\"title\":\"Go to California\",\"timing\":15,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid title", value = "{\"id\":null,\"title\":\"  \",\"timing\":15,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid timing", value = "{\"id\":null,\"title\":\"Go to California\",\"timing\":-3,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid producer", value = "{\"id\":null,\"title\":\"Go to California\",\"timing\":15,\"producer\":\"ggggggggggggggggggggggggggggggggggggggggg\"}"),
                    @ExampleObject(name = "Not valid timing, must be not null", value = "{\"id\":1,\"title\":\"Go to California\",\"timing\":null,\"producer\":\"Petro\"}")
            })) MovieDto dto) {
        return service.create(dto);
    }

    @Operation(summary = "Gets movie", description = "Gets movie by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie is found", content = @Content(schema = @Schema(implementation = MovieDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter"),
            @ApiResponse(responseCode = "404", description = "Movie by id not fount")
    })
    @Override
    public MovieDto getById(@Parameter(description = "Movie id. Not null and greater then 0", examples = {
            @ExampleObject(name = "Valid id", value = "1"),
            @ExampleObject(name = "Invalid id,less 0", value = "-5"),
            @ExampleObject(name = "Invalid id, null", value = "null"),
            @ExampleObject(name = "Not found", value = "1500")
    }) Long id) {
        validateParam(id);
        return service.getById(id);
    }

    @Operation(summary = "Updates existed movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie is updated", content = @Content(schema = @Schema(implementation = MovieDto.class))),
            @ApiResponse(responseCode = "400", description = "Not valid dto"),
            @ApiResponse(responseCode = "404", description = "Movie by id not fount")})
    @Override
    public MovieDto update(@RequestBody(description = "Movie dto for updating",
            content = @Content(examples = {
                    @ExampleObject(name = "Valid dto", value = "{\"id\":3,\"title\":\"Go to California\",\"timing\":15,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not found", value = "{\"id\":1500,\"title\":\"Go to California\",\"timing\":15,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid id", value = "{\"id\":null,\"title\":\"Go to California\",\"timing\":15,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid title", value = "{\"id\":3,\"title\":\"  \",\"timing\":15,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid timing", value = "{\"id\":3,\"title\":\"Go to California\",\"timing\":-3,\"producer\":\"Petro\"}"),
                    @ExampleObject(name = "Not valid producer", value = "{\"id\":3,\"title\":\"Go to California\",\"timing\":15,\"producer\":\"ggggggggggggggggggggggggggggggggggggggggg\"}"),
                    @ExampleObject(name = "Not valid timing, must be not null", value = "{\"id\":3,\"title\":\"Go to California\",\"timing\":null,\"producer\":\"Petro\"}")
            })
    ) MovieDto dto) {
        return service.update(dto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(value = "\"Movie has removed successfully\""))),
            @ApiResponse(responseCode = "400", headers = @Header(name = "Error message", schema = @Schema(implementation = String.class, example = "Argument is invalid. Number must be not null and greater then 0"))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = String.class, example = "Movie is not found by id")))
    })
    @Override
    public ResponseEntity<Object> delete(@Parameter(description = "Movie id. Not null and greater then 0", examples = {
            @ExampleObject(name = "Valid id", value = "6"),
            @ExampleObject(name = "Invalid id,less 0", value = "-5"),
            @ExampleObject(name = "Invalid id, null", value = "null"),
            @ExampleObject(name = "Not found", value = "1500")
    }) Long id) {
        validateParam(id);
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Movie has removed successfully");
    }

    @Operation(summary = "Searches movies by filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie is found", content = @Content(schema = @Schema(implementation = MovieDto.class)))})
    @Override
    public Page<MovieDto> getByFilter(
            @RequestBody(description = "Flag active(true) means that you retrieve movies which sessions will start from this moment only",
                    content = @Content(schema = @Schema(implementation = MovieQueryFilter.class),
                            examples = {
                                    @ExampleObject(name = "Dark of the moon", value = "{\"page\":0,\"limit\":10,\"sortingAscending\":false,\"title\":\"of\",\"producer\":null,\"active\":false}"),
                                    @ExampleObject(description = "Must not contain movie with id 6", name = "Active films", value = "{\"page\":0,\"limit\":10,\"sortingAscending\":false,\"title\":null,\"producer\":null,\"active\":true}"),
                                    @ExampleObject(description = "Producer is at least Nelson Shin", name = "By producer", value = "{\"page\":0,\"limit\":10,\"sortingAscending\":false,\"title\":null,\"producer\":\"Nel\",\"active\":false}")
                            }))
                    MovieQueryFilter filter) {
        return service.getByFiler(filter);
    }
}
