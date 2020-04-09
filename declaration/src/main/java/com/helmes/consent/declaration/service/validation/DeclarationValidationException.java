package com.helmes.consent.declaration.service.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeclarationValidationException extends RuntimeException {
    public DeclarationValidationException(String message) {
        super(message);
    }
}
