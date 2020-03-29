package com.helmes.consent.service.service.impl;

import com.helmes.consent.service.ServiceApplication;
import com.helmes.consent.service.domain.Service;
import com.helmes.consent.service.repository.ServiceRepository;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.server.model.ServiceDeclarationResponse;
import com.helmes.consent.service.service.ServiceService;
import com.helmes.consent.service.service.mapper.ServiceMapper;
import com.helmes.consent.service.service.validation.ServiceDeclarationRequestValidator;
import com.helmes.consent.service.service.validation.ServiceValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest(classes = ServiceApplication.class)
class ServiceServiceImplTest {
    final String SERVICE_DECLARATION_ID = "serviceDeclarationId";
    final String SERVICE_PROVIDER_ID = "serviceProviderId";
    final int CONSENT_MAX_DURATION = 0;
    final int MAX_CACHE = 0;
    final String DESCRIPTION = "description";
    final String NAME = "name";
    final boolean DOES_NOT_NEED_SIG = false;
    final String TECHNICAL_DESCRIPTION = "E";
    final long VALID_UNTIL_FUTURE = Instant.now().plusSeconds(60*60).getEpochSecond();

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
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        final ServiceDeclarationResponse response = this.serviceService.save(sdr);
        assertThat(response).isNotNull();
        assertThat(response.getResponse()).isEqualTo("OK");
    }
    @Test
    void shouldNotSave_uniqueConstraintException() {
        doThrow(DataIntegrityViolationException.class).when(repository).save(any());
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> {
            this.serviceService.save(sdr);
        });
        Assertions.assertThat(exception.getMessage()).isEqualTo("duplicate_declaration");
    }
}
