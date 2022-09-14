package com.example.cinema.core;

import org.springframework.util.StringUtils;

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
}
