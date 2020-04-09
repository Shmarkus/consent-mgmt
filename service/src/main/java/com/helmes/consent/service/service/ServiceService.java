package com.helmes.consent.service.service;

import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.service.impl.ServiceDuplicateDeclarationException;
import com.helmes.consent.service.service.validation.ServiceValidationException;

/**
 * Service Declaration Request-related services
 */
public interface ServiceService {
    /**
     * Validate service declaration request and save it to database
     *
     * @param request Request to save
     * @return ServiceDeclarationResponse when validation passes and request is stored to database
     * @throws ServiceValidationException           When validation fails
     * @throws ServiceDuplicateDeclarationException When database save fails
     */
    ServiceDeclarationRequest save(ServiceDeclarationRequest request) throws ServiceValidationException, ServiceDuplicateDeclarationException;
}
