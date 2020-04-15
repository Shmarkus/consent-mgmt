package com.helmes.consent.declaration.service;

import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.service.impl.exception.DuplicateDeclarationException;
import com.helmes.consent.declaration.service.validation.exception.DeclarationValidationException;

/**
 * Service Declaration Request-related services
 */
public interface DeclarationService {
    /**
     * Validate service declaration request and save it to database
     *
     * @param request Request to save
     * @return ServiceDeclaration When validation passes and request is stored to database
     * @throws DeclarationValidationException When validation fails
     * @throws DuplicateDeclarationException  When database save fails
     */
    ServiceDeclaration save(ServiceDeclaration request) throws DeclarationValidationException, DuplicateDeclarationException;
}
