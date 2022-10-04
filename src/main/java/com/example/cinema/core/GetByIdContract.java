package com.example.cinema.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Interface that provides contract of retrieving entity by id.
 *
 * @param <T> {@link DtoMarker}
 * @author Alexandr Yefremov
 */
public interface GetByIdContract<T extends DtoMarker> {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "${url.get-by-id}", produces = APPLICATION_JSON_VALUE)
    T getById(@PathVariable(name = "id") Long id);
}
