package com.example.cinema.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Contract for controllers that provide creating and updating operations.
 *
 * @param <T> {@link DtoMarker}
 * @author Alexandr Yefremov
 */
public interface CreateUpdateContract<T extends DtoMarker> extends GetByIdContract<T> {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "${url.create}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    T create(@RequestBody T dto);

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = "${url.update}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    T update(@RequestBody T dto);

}
