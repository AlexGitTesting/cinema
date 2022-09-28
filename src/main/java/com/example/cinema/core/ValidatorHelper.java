package com.example.cinema.core;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Helper, for parameter validation.
 *
 * @author Alexandr Yefremov
 */
public interface ValidatorHelper {
    /**
     * Validate short.
     *
     * @param s not null and greater then 0
     * @throws IllegalArgumentException if param is not correct
     */
    static void validateShort(Short s) throws IllegalArgumentException {
        if (s == null || s < 1) throw new IllegalArgumentException("Argument is invalid");
    }

    /**
     * Validate long.
     *
     * @param s not null and greater then 0
     * @throws IllegalArgumentException if param is not correct
     */
    static void validateLong(Long s) throws IllegalArgumentException {
        if (s == null || s < 1) throw new IllegalArgumentException("Argument is invalid");
    }
// FIXME: 28.09.2022 remove
//    static void validateString(String s)throws IllegalArgumentException {
//        if (!StringUtils.hasLength(s)) throw new IllegalArgumentException("Argument is invalid");
//    }

    /**
     * Validates id.
     *
     * @param id id
     * @throws ResponseStatusException if id is null or less then 1
     */
    static void validateParam(Long id) throws ResponseStatusException {
        if (id == null || id < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id, must be not null and greater then 0");
    }
}
