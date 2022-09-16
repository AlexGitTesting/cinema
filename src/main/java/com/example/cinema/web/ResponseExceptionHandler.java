package com.example.cinema.web;

import com.example.cinema.core.ValidationCustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
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
    private Logger log = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    @Autowired
    public ResponseExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationCustomException.class)
    public ResponseEntity<Object> handle(final ValidationCustomException e) {
        return ResponseEntity.badRequest().body(makeResponseMessage(e));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handle(final EntityNotFoundException e) {
        return handleExceptionInternal(e, e.getMessage(), HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handle(final IllegalStateException e) {

        return ResponseEntity.badRequest().body(getMessageSourceAccessor().getMessage(e.getMessage(), e.getMessage(), request.getLocale()));
    }


    private String makeResponseMessage(ValidationCustomException e) {
        StringBuilder message = new StringBuilder("Validation exception:  \n");
        final Map<String, String> messageMap = e.getMessageMap();
        for (Map.Entry<String, String> entry :
                messageMap.entrySet()) {
            final Locale locale = request.getLocale();
            message.append(format("Field: %s  message: %s, \n", entry.getKey(),
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
