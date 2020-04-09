package com.helmes.consent.service.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ServiceDuplicateDeclarationException extends RuntimeException {
    public ServiceDuplicateDeclarationException(String message) {
        super(message);
    }
}
