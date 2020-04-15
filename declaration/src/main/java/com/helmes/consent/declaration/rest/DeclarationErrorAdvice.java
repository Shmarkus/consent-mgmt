package com.helmes.consent.declaration.rest;

import com.helmes.consent.declaration.server.model.ErrorResponse;
import com.helmes.consent.declaration.service.impl.exception.DuplicateDeclarationException;
import com.helmes.consent.declaration.service.validation.exception.DeclarationValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.OffsetDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@ControllerAdvice
public class DeclarationErrorAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DuplicateDeclarationException.class})
    public ResponseEntity<ErrorResponse> handleException(DuplicateDeclarationException e) {
        log.error("Caught exception: {}", e.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setError("duplicate_declaration");
        response.setMessage(e.getMessage());
        response.setTimestamp(OffsetDateTime.now());
        response.setStatus(400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DeclarationValidationException.class})
    public ResponseEntity<ErrorResponse> handleException(DeclarationValidationException e) {
        log.error("Caught exception: {}", e.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setError("invalid_request");
        response.setMessage(e.getMessage());
        response.setTimestamp(OffsetDateTime.now());
        response.setStatus(400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        log.error("Caught exception: {}", e.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setError("invalid_request");
        response.setMessage(e.getMessage());
        response.setTimestamp(OffsetDateTime.now());
        response.setStatus(400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
