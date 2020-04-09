package com.helmes.consent.service.rest;

import com.helmes.consent.service.ServiceApplication;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.server.model.ServiceDeclarationResponse;
import com.helmes.consent.service.service.impl.ServiceDuplicateDeclarationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ServiceApplication.class)
class ServiceResourceIntegrationTest {
    final String SERVICE_DECLARATION_ID = "serviceDeclarationId";
    final String SERVICE_PROVIDER_ID = "serviceProviderId";
    final int CONSENT_MAX_DURATION = 0;
    final int MAX_CACHE = 0;
    final String DESCRIPTION = "description";
    final String NAME = "name";
    final boolean DOES_NOT_NEED_SIG = false;
    final String TECHNICAL_DESCRIPTION = "E";
    final long VALID_UNTIL_FUTURE = Instant.now().plusSeconds(60 * 60).getEpochSecond();

    @Autowired
    private ServiceResource serviceResource;

    @Test
    void shouldAddValidServiceDeclaration() {
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
        ResponseEntity<ServiceDeclarationResponse> responseEntity = serviceResource.addServiceDeclaration(sdr);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldNotAddDuplicateDeclaration() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId("DuplicateDeclaration")
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        serviceResource.addServiceDeclaration(sdr);

        ServiceDuplicateDeclarationException exception = assertThrows(ServiceDuplicateDeclarationException.class, () -> {
            serviceResource.addServiceDeclaration(sdr);
        });
        assertThat(exception.getMessage()).isEqualTo("duplicate_declaration");
    }
}
