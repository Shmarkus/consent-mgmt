package com.helmes.consent.declaration.service.validation;

import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.service.validation.exception.DeclarationValidationException;

/**
 * Validator for Service Declaration
 */
public interface DeclarationRequestValidator {
    /**
     * Validate Service Declaration request fields
     * <p>
     * When validation passes, the method returns true. When validation wails, the method throws a ServiceValidationException
     *
     * @param request Request to validate
     * @return true when validation succeeds
     * @throws DeclarationValidationException when validation fails
     */
    boolean isValid(ServiceDeclaration request) throws DeclarationValidationException;
}
