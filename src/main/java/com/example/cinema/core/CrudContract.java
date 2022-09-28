package com.example.cinema.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Contract for base operations for controllers.
 *
 * @param <T> {@link DtoMarker}
 * @author Alexandr Yefremov
 */
public interface CrudContract<T extends DtoMarker> {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "${url.create}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    T create(@RequestBody T dto);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "${url.get-by-id}", produces = APPLICATION_JSON_VALUE)
    T getById(@PathVariable(name = "id") Long id);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "${url.update}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    T update(@RequestBody T dto);


}
