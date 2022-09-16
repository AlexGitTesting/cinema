package com.example.cinema.core;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

public interface ValidatorHelper {

    static void validateShort(Short s) {
        if (s == null || s < 1) throw new IllegalArgumentException("Argument is invalid");
    }

    static void validateLong(Long s) {
        if (s == null || s < 1) throw new IllegalArgumentException("Argument is invalid");
    }

    static void validateString(String s) {
        if (!StringUtils.hasLength(s)) throw new IllegalArgumentException("Argument is invalid");
    }

    static void validateParam(Long id) {
        if (id == null || id < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id, must be not null and greater then 0");
    }
}
