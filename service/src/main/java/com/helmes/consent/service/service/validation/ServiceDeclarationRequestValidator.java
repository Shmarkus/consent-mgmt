package com.helmes.consent.service.service.validation;

import com.helmes.consent.service.server.model.ServiceDeclarationRequest;

/**
 * Validator for Service Declaration
 */
public interface ServiceDeclarationRequestValidator {
    /**
     * Validate Service Declaration request fields
     * <p>
     * When validation passes, the method returns true. When validation wails, the method throws a ServiceValidationException
     *
     * @param request Request to validate
     * @return true when validation succeeds
     * @throws ServiceValidationException when validation fails
     */
    boolean isValid(ServiceDeclarationRequest request) throws ServiceValidationException;
}
