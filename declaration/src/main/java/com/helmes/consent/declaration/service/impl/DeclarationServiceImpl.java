package com.helmes.consent.declaration.service.impl;

import com.helmes.consent.declaration.domain.Declaration;
import com.helmes.consent.declaration.feign.api.ServiceProviderApi;
import com.helmes.consent.declaration.repository.DeclarationRepository;
import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.service.DeclarationService;
import com.helmes.consent.declaration.service.impl.exception.DuplicateDeclarationException;
import com.helmes.consent.declaration.service.mapper.DeclarationMapper;
import com.helmes.consent.declaration.service.validation.DeclarationRequestValidator;
import com.helmes.consent.declaration.service.validation.exception.DeclarationValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeclarationServiceImpl implements DeclarationService {
    private final DeclarationMapper declarationMapper;
    private final DeclarationRepository declarationRepository;
    private final DeclarationRequestValidator declarationRequestValidator;
    private final ServiceProviderApi serviceProviderApi;

    public DeclarationServiceImpl(DeclarationMapper declarationMapper, DeclarationRepository declarationRepository, DeclarationRequestValidator declarationRequestValidator, ServiceProviderApi serviceProviderApi) {
        this.declarationMapper = declarationMapper;
        this.declarationRepository = declarationRepository;
        this.declarationRequestValidator = declarationRequestValidator;
        this.serviceProviderApi = serviceProviderApi;
    }

    @Override
    public ServiceDeclaration save(ServiceDeclaration request) throws DeclarationValidationException, DuplicateDeclarationException {
        log.debug("Saving declaration request {}", request);
        try {
            serviceProviderApi.getServiceProviderByServiceProviderId(request.getServiceProviderId());
        } catch (Exception e) {
            throw new DeclarationValidationException(e.getMessage());
        }
        if (!declarationRequestValidator.isValid(request)) {
            throw new DeclarationValidationException("Field requirements not met");
        }
        try {
            Declaration declaration = declarationRepository.save(declarationMapper.toEntity(request));
            return declarationMapper.toDto(declaration);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDeclarationException(e.getMessage());
        }
    }
}
