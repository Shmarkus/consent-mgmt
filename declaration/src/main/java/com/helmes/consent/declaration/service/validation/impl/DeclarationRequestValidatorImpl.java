package com.helmes.consent.declaration.service.validation.impl;

import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.service.validation.DeclarationRequestValidator;
import com.helmes.consent.declaration.service.validation.exception.DeclarationValidationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Instant;
import java.util.Set;

@Component
public class DeclarationRequestValidatorImpl implements DeclarationRequestValidator {
    @Override
    public boolean isValid(ServiceDeclaration request) throws DeclarationValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ServiceDeclaration>> violations = validator.validate(request);
        return violations.isEmpty() &&
                (request.getValidUntil() != null && request.getValidUntil() > Instant.now().getEpochSecond()) &&
                request.getMaxCacheSeconds() >= 0;
    }
}
