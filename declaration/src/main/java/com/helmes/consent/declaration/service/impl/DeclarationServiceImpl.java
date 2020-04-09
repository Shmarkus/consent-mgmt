package com.helmes.consent.declaration.service.impl;

import com.helmes.consent.declaration.domain.Declaration;
import com.helmes.consent.declaration.repository.DeclarationRepository;
import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.service.DeclarationService;
import com.helmes.consent.declaration.service.mapper.DeclarationMapper;
import com.helmes.consent.declaration.service.validation.DeclarationRequestValidator;
import com.helmes.consent.declaration.service.validation.DeclarationValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeclarationServiceImpl implements DeclarationService {
    private final DeclarationMapper declarationMapper;
    private final DeclarationRepository declarationRepository;
    private final DeclarationRequestValidator declarationRequestValidator;

    public DeclarationServiceImpl(DeclarationMapper declarationMapper, DeclarationRepository declarationRepository, DeclarationRequestValidator declarationRequestValidator) {
        this.declarationMapper = declarationMapper;
        this.declarationRepository = declarationRepository;
        this.declarationRequestValidator = declarationRequestValidator;
    }

    @Override
    public ServiceDeclaration save(ServiceDeclaration request) throws DeclarationValidationException, DuplicateDeclarationException {
        log.debug("Saving declaration request {}", request);
        if (!declarationRequestValidator.isValid(request)) {
            log.error("Unable to save, request is not valid");
            throw new DeclarationValidationException("invalid_request");
        }
        try {
            Declaration declaration = declarationRepository.save(declarationMapper.toEntity(request));
            return declarationMapper.toDto(declaration);
        } catch (DataIntegrityViolationException e) {
            log.error("Unable to save: {}", e.getMessage());
            throw new DuplicateDeclarationException("duplicate_declaration");
        }
    }
}
