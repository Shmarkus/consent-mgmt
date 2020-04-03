package com.helmes.consent.service.service;

import com.helmes.consent.service.service.validation.ServiceValidationException;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.server.model.ServiceDeclarationResponse;

/**
 * Service Declaration Request-related services
 */
public interface ServiceService {
    /**
     * Validate service declaration request and save it to database
     * @param request Request to save
     * @return ServiceDeclarationResponse when validation passes and request is stored to database
     * @throws ServiceValidationException When validation or database save fails
     */
    ServiceDeclarationResponse save(ServiceDeclarationRequest request) throws ServiceValidationException;
}
