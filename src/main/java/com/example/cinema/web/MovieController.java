package com.example.cinema.web;

import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.dto.MovieDto;
import com.example.cinema.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.cinema.core.ValidatorHelper.validateParam;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Handles HTTP requests connected with Movie.
 *
 * @author Alexandr Yefremov
 */
@RestController
@RequestMapping(path = "/movie", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
@Tag(name = "Movie controller", description = "Handles HTTP requests connected with Movie")
public class MovieController {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @Operation(summary = "Creates new movie")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create.json")
    MovieDto create(@RequestBody MovieDto dto) {
        return service.create(dto);
    }

    @Operation(summary = "Gets movie", description = "Gets movie by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}/get-movie.json")
    MovieDto getById(@Parameter(description = "Movie id. Not null and greater then 0") @PathVariable("id") Long id) {
        validateParam(id);
        return service.getById(id);
    }

    @Operation(summary = "Updates existed movie")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "/update.json")
    MovieDto update(@RequestBody MovieDto dto) {
        return service.update(dto);
    }

    @Operation(summary = "Searches movies by filter")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/filter.json")
    Page<MovieDto> getByFilter(@Parameter(description = "Flag isActive(true) means you retrieve movies not yet started ")
                               @RequestBody MovieQueryFilter filter) {
        return service.getByFiler(filter);
    }

    @Operation(summary = "Removes movie by id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/delete.json")
    boolean delete(@Parameter(description = "Movie id. Not null and greater then 0") @PathVariable("id") Long id) {
        validateParam(id);
        return service.delete(id);
    }


}
