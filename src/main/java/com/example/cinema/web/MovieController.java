package com.example.cinema.web;

import com.example.cinema.core.CrudContract;
import com.example.cinema.core.DeleteContract;
import com.example.cinema.core.FilterableContract;
import com.example.cinema.dao.MovieQueryFilter;
import com.example.cinema.dto.MovieDto;
import com.example.cinema.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
public class MovieController implements CrudContract<MovieDto>, DeleteContract, FilterableContract<MovieDto, MovieQueryFilter> {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @Operation(summary = "Creates new movie")
    @Override
    public MovieDto create(@Parameter(description = "Movie dto") MovieDto dto) {
        return service.create(dto);
    }

    @Operation(summary = "Gets movie", description = "Gets movie by id")
    @Override
    public MovieDto getById(@Parameter(description = "Movie id. Not null and greater then 0") Long id) {
        validateParam(id);
        return service.getById(id);
    }

    @Operation(summary = "Updates existed movie")
    @Override
    public MovieDto update(MovieDto dto) {
        return service.update(dto);
    }

    @Override
    public ResponseEntity<Object> delete(@Parameter(description = "Movie id. Not null and greater then 0") Long id) {
        validateParam(id);
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Movie has removed successfully");
    }

    @Operation(summary = "Searches movies by filter")
    @Override
    public Page<MovieDto> getByFilter(@Parameter(description = "Flag isActive(true) means you retrieve movies not yet started or finished") MovieQueryFilter filter) {
        return service.getByFiler(filter);
    }
}
