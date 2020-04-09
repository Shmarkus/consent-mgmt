package com.helmes.consent.service.service.impl;

import com.helmes.consent.service.repository.ServiceRepository;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.service.ServiceService;
import com.helmes.consent.service.service.mapper.ServiceMapper;
import com.helmes.consent.service.service.validation.ServiceDeclarationRequestValidator;
import com.helmes.consent.service.service.validation.ServiceValidationException;
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
    public ServiceDeclarationRequest save(ServiceDeclarationRequest request) throws ServiceValidationException, ServiceDuplicateDeclarationException {
        log.debug("Saving declaration request {}", request);
        if (!serviceDeclarationRequestValidator.isValid(request)) {
            log.error("Unable to save, request is not valid");
            throw new ServiceValidationException("invalid_request");
        }
        try {
            com.helmes.consent.service.domain.Service service = serviceRepository.save(serviceMapper.toEntity(request));
            return serviceMapper.toDto(service);
        } catch (DataIntegrityViolationException e) {
            log.error("Unable to save: {}", e.getMessage());
            throw new ServiceDuplicateDeclarationException("duplicate_declaration");
        }
    }
}
