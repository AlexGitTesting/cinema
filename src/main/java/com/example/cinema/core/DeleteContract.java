package com.example.cinema.core;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Contract provides functionality of  removing entity by id.
 *
 * @author Alexandr Yefremov
 */
public interface DeleteContract {

    @Operation(summary = "Removes entity by id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "${url.delete}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> delete(@PathVariable(name = "id") Long id);
}
