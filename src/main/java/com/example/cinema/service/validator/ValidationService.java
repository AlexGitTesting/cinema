package com.example.cinema.service.validator;


import com.example.cinema.core.ValidationCustomException;

/**
 * Validates objects using bean validation.
 *
 * @author Alexandr Yefremov
 */
public interface ValidationService {
    void validate(Object target, String objectName, Object... validationHints) throws ValidationCustomException;
}
