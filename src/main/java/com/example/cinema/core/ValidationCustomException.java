package com.example.cinema.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom exception can be thrown after bean validation and converted from ConstraintViolationException..
 * Contains map where key is a field name, value is a message.
 *
 * @author Alexandr Yefremov
 */
public class ValidationCustomException extends RuntimeException {

    private final static String standardMessage = "During  validation error occurred!";
    private final Map<String, String> messageMap = new HashMap<>();

    public ValidationCustomException() {
    }

    public ValidationCustomException(Map<String, String> messageMap) {
        super(standardMessage);
        this.messageMap.putAll(messageMap);
    }

    public ValidationCustomException(Throwable cause) {
        super(cause);
    }

    public Map<String, String> getMessageMap() {
        return messageMap;
    }
}
