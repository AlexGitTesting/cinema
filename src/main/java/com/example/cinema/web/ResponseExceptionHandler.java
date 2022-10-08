package com.example.cinema.web;

import com.example.cinema.core.CustomConstraintException;
import com.example.cinema.core.ValidationCustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.Locale;
import java.util.Map;

import static java.lang.String.format;

/**
 * Handler class to handle exceptions.
 *
 * @author Alexandr Yefremov
 */
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private final MessageSource messageSource;
    @Resource
    protected WebRequest request;
    private MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public ResponseExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationCustomException.class)
    public ResponseEntity<String> handle(final ValidationCustomException e) {
        return ResponseEntity.badRequest().body(makeResponseMessage(e));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handle(final EntityNotFoundException e) {
        return ResponseEntity.status(404).body(getBody(e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(final IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(getBody(e));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomConstraintException.class)
    public ResponseEntity<String> handle(final CustomConstraintException e) {
        return ResponseEntity.badRequest().body(getMessageSourceAccessor().getMessage(e.getCustomMessage()
                , e.getArgs()
                , e.getMessage()
                , request.getLocale()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handle(final IllegalStateException e) {
        return ResponseEntity.badRequest().body(getBody(e));
    }

    /**
     * Prepares localized message retrieved from {@link Exception#getMessage()}.
     *
     * @param e exception
     * @return localized or default message
     */
    private String getBody(Exception e) {
        return getMessageSourceAccessor().getMessage(e.getMessage(), e.getMessage(), request.getLocale());
    }

    /**
     * Prepares localized and formatted message with arguments.
     *
     * @param languageVar language variable that will be resolved to get message by locale
     * @param defMessage  default message will be used if there are not language variables
     * @param args        args will be used to fill in messages
     * @return string of the message
     */
    public String prepareLocalizedMessage(String languageVar, String defMessage, Object[] args) {
        return getMessageSourceAccessor().getMessage(languageVar, args, defMessage, request.getLocale());
    }

    /**
     * Builds localized string message using {@link ValidationCustomException#getMessageMap()}
     *
     * @param e {@link ValidationCustomException}
     * @return string message
     */
    private String makeResponseMessage(ValidationCustomException e) {
        StringBuilder message = new StringBuilder("Validation exception:  \n");
        final Map<String, String> messageMap = e.getMessageMap();
        for (Map.Entry<String, String> entry :
                messageMap.entrySet()) {
            final Locale locale = request.getLocale();
            message.append(format("Field: %s,  message: %s, \n", entry.getKey(),
                    getMessageSourceAccessor().getMessage(entry.getValue(), e.getMessage(), locale)));
        }
        return message.toString();
    }

    private MessageSourceAccessor getMessageSourceAccessor() {
        if (messageSourceAccessor == null) {

            messageSourceAccessor = new MessageSourceAccessor(messageSource);
        }
        return messageSourceAccessor;
    }
}
