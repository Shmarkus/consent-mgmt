package com.helmes.consent.service.service.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ServiceValidationException extends RuntimeException {
    public ServiceValidationException(String message) {
        super(message);
    }
}
