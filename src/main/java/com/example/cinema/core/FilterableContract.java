package com.example.cinema.core;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Contract for controllers that implement queries by filter.
 *
 * @param <D> {@link DtoMarker}
 * @param <F> {@link FilterMarker}
 * @author Alexandr Yefremov
 */
public interface FilterableContract<D extends DtoMarker, F extends FilterMarker> {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "${url.get-by-filter}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Page<D> getByFilter(@RequestBody F filter);
}
