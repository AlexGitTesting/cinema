package com.example.cinema.service.validator;

import com.example.cinema.core.ValidationCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the {@link ValidationService}
 *
 * @author Alexandr Yefremov
 */
@Service
public class ValidationServiceImpl implements ValidationService {
    private static Logger log = LoggerFactory.getLogger(ValidationServiceImpl.class);
    private final SmartValidator validator;


    @Autowired
    public ValidationServiceImpl(SmartValidator validator) {
        this.validator = validator;

    }

    @Override
    public void validate(Object target, String objectName, Object... validationHints) throws ValidationCustomException {
        final MapBindingResult mapBindingResult = new MapBindingResult(new LinkedHashMap<String, String>(), objectName);
        validator.validate(target, mapBindingResult, validationHints);
        if (mapBindingResult.hasErrors()) {
            final Map<String, String> messageMap = new HashMap<>();
            for (ObjectError objectError : mapBindingResult.getAllErrors()) {
                if (objectError instanceof FieldError) {
                    messageMap.put(((FieldError) objectError).getField(), objectError.getDefaultMessage());
                    if (objectError.getCodes() != null && objectError.getCodes().length > 0) {
                        Arrays.stream(objectError.getCodes()).forEach(s -> log.debug(s));
                        log.debug(objectError.getDefaultMessage());
                    }
                } else {
                    messageMap.put(objectError.getCode(), objectError.getDefaultMessage());
                }
            }
            throw new ValidationCustomException(messageMap);
        }
    }


}
