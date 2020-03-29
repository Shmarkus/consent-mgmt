package com.helmes.consent.service.service.impl;

import com.helmes.consent.service.service.validation.ServiceDeclarationRequestValidator;
import com.helmes.consent.service.service.validation.ServiceValidationException;
import com.helmes.consent.service.repository.ServiceRepository;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.server.model.ServiceDeclarationResponse;
import com.helmes.consent.service.service.ServiceService;
import com.helmes.consent.service.service.mapper.ServiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceServiceImpl implements ServiceService {
    private final ServiceMapper serviceMapper;
    private final ServiceRepository serviceRepository;
    private final ServiceDeclarationRequestValidator serviceDeclarationRequestValidator;

    public ServiceServiceImpl(ServiceMapper serviceMapper, ServiceRepository serviceRepository, ServiceDeclarationRequestValidator serviceDeclarationRequestValidator) {
        this.serviceMapper = serviceMapper;
        this.serviceRepository = serviceRepository;
        this.serviceDeclarationRequestValidator = serviceDeclarationRequestValidator;
    }

    @Override
    public ServiceDeclarationResponse save(ServiceDeclarationRequest request) throws ServiceValidationException {
        log.debug("Saving declaration request {}", request);
        serviceDeclarationRequestValidator.isValid(request);
        ServiceDeclarationResponse response = new ServiceDeclarationResponse();
        try {
            serviceRepository.save(serviceMapper.toEntity(request));
            response.setResponse("OK");
        } catch (DataIntegrityViolationException e) {
            log.error("Unable to save: {}", e.getMessage());
            throw new ServiceValidationException("duplicate_declaration");
        }
        return response;
    }
}
