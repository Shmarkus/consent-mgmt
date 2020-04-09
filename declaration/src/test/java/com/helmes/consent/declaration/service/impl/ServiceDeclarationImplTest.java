package com.helmes.consent.declaration.service.impl;

import com.helmes.consent.declaration.DeclarationApplication;
import com.helmes.consent.declaration.domain.Declaration;
import com.helmes.consent.declaration.feign.api.ServiceProviderApi;
import com.helmes.consent.declaration.repository.DeclarationRepository;
import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.service.DeclarationService;
import com.helmes.consent.declaration.service.mapper.DeclarationMapper;
import com.helmes.consent.declaration.service.validation.DeclarationRequestValidator;
import com.helmes.consent.declaration.service.validation.DeclarationValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(classes = DeclarationApplication.class)
class ServiceDeclarationImplTest {
    @Mock
    private DeclarationRepository repository;

    @Autowired
    private DeclarationMapper declarationMapper;

    @Mock
    private DeclarationRequestValidator validator;

    @Mock
    private ServiceProviderApi serviceProviderApi;

    private DeclarationService declarationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(new Declaration()).when(repository).save(any());
        doReturn(true).when(validator).isValid(any());
        this.declarationService = new DeclarationServiceImpl(declarationMapper, repository, validator, serviceProviderApi);
    }

    @Test
    void shouldSave() {
        final ServiceDeclaration response = this.declarationService.save(new ServiceDeclaration());
        assertThat(response).isNotNull();
    }

    @Test
    void shouldNotSave_uniqueConstraintException() {
        doThrow(DataIntegrityViolationException.class).when(repository).save(any());
        assertThrows(DuplicateDeclarationException.class, () -> this.declarationService.save(new ServiceDeclaration()));
    }

    @Test
    void shouldNotSave_notValidCache() {
        doReturn(false).when(validator).isValid(any());
        assertThrows(DeclarationValidationException.class, () -> this.declarationService.save(new ServiceDeclaration()));
    }
}
