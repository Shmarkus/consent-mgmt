package com.helmes.consent.service.service.validation;

import com.helmes.consent.service.server.model.ServiceDeclarationRequest;

public interface ServiceDeclarationRequestValidator {
    boolean isValid(ServiceDeclarationRequest request) throws ServiceValidationException;
}
