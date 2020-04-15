package com.helmes.consent.declaration.service.impl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateDeclarationException extends RuntimeException {
    public DuplicateDeclarationException(String message) {
        super(message);
    }
}
