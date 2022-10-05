package com.example.cinema.core;

import org.springframework.util.StringUtils;

/**
 * Custom exception can be thrown if DB constraints were violated during saving the entity.
 *
 * @author Alexandr Yefremov
 */
public class CustomConstraintException extends RuntimeException {
    private final static String standardMessage = "The constraint was violated while saving the entity!";
    private final String customMessage;
    private final Object[] args;


    public CustomConstraintException(String customMessage, Object[] args) {
        super(!StringUtils.hasText(customMessage)
                ? standardMessage
                : customMessage);
        this.customMessage = customMessage;
        this.args = args;
    }

    public CustomConstraintException(Throwable cause, String customMessage, Object[] args) {
        super(!StringUtils.hasText(customMessage)
                ? standardMessage
                : customMessage, cause);
        this.customMessage = customMessage;
        this.args = args;
    }


    public String getStandardMessage() {
        return standardMessage;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public Object[] getArgs() {
        return args;
    }

}
