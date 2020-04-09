package com.helmes.consent.service.service.impl;

import com.helmes.consent.service.ServiceApplication;
import com.helmes.consent.service.domain.Service;
import com.helmes.consent.service.repository.ServiceRepository;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.service.ServiceService;
import com.helmes.consent.service.service.mapper.ServiceMapper;
import com.helmes.consent.service.service.validation.ServiceDeclarationRequestValidator;
import com.helmes.consent.service.service.validation.ServiceValidationException;
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

@SpringBootTest(classes = ServiceApplication.class)
class ServiceServiceImplTest {
    @Mock
    private ServiceRepository repository;

    @Autowired
    private ServiceMapper serviceMapper;

    @Mock
    private ServiceDeclarationRequestValidator validator;

    private ServiceService serviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(new Service()).when(repository).save(any());
        doReturn(true).when(validator).isValid(any());
        this.serviceService = new ServiceServiceImpl(serviceMapper, repository, validator);
    }

    @Test
    void shouldSave() {
        final ServiceDeclarationRequest response = this.serviceService.save(new ServiceDeclarationRequest());
        assertThat(response).isNotNull();
    }

    @Test
    void shouldNotSave_uniqueConstraintException() {
        doThrow(DataIntegrityViolationException.class).when(repository).save(any());
        assertThrows(ServiceValidationException.class, () -> {
            this.serviceService.save(new ServiceDeclarationRequest());
        });
    }

    @Test
    void shouldNotSave_notValidCache() {
        doReturn(false).when(validator).isValid(any());
        assertThrows(ServiceDuplicateDeclarationException.class, () -> {
            this.serviceService.save(new ServiceDeclarationRequest());
        });
    }
}
