package com.helmes.consent.service.service.validation;

import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Instant;
import java.util.Set;

@Component
public class ServiceDeclarationRequestValidatorImpl implements ServiceDeclarationRequestValidator {
    @Override
    public boolean isValid(ServiceDeclarationRequest request) throws ServiceValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ServiceDeclarationRequest>> violations = validator.validate(request);
        if ((request.getValidUntil() != null &&
            request.getValidUntil() < Instant.now().getEpochSecond()) ||
            request.getMaxCacheSeconds() < 0 ||
            !violations.isEmpty()) {
            throw new ServiceValidationException("invalid_request");
        }
        return true;
    }
}
