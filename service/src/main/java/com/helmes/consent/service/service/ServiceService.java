package com.helmes.consent.service.service;

import com.helmes.consent.service.service.validation.ServiceValidationException;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.server.model.ServiceDeclarationResponse;

public interface ServiceService {
    ServiceDeclarationResponse save(ServiceDeclarationRequest request) throws ServiceValidationException;
}
