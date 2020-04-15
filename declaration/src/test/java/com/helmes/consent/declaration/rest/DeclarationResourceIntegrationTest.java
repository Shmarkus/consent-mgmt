package com.helmes.consent.declaration.rest;

import com.helmes.consent.declaration.feign.model.ServiceProvider;
import com.helmes.consent.declaration.server.model.ErrorResponse;
import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.server.model.ServiceDeclarationResponse;
import com.helmes.consent.declaration.service.feign.ServiceProviderApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DeclarationResourceIntegrationTest {
    private static final String URL = "/declaration";
    private static final String INVALID_REQUEST = "invalid_request";
    private static final String DUPLICATE_DECLARATION = "duplicate_declaration";

    private static final String SERVICE_DECLARATION_ID = "serviceDeclarationId";
    private static final String SERVICE_PROVIDER_ID = "serviceProviderId";
    private static final int CONSENT_MAX_DURATION = 0;
    private static final int MAX_CACHE = 0;
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final boolean DOES_NOT_NEED_SIG = false;
    private static final String TECHNICAL_DESCRIPTION = "E";
    private static final long VALID_UNTIL_FUTURE = Instant.now().plusSeconds(60 * 60).getEpochSecond();

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ServiceProviderApiClient serviceProviderApiClientMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(ResponseEntity.ok(new ServiceProvider())).when(serviceProviderApiClientMock).getServiceProviderByServiceProviderId(any());
    }

    @Test
    void shouldAdd_validServiceDeclaration() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);

        ResponseEntity<ServiceDeclarationResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ServiceDeclarationResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getResponse()).isEqualTo("OK");
    }

    @Test
    void shouldNotAdd_duplicateDeclaration() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId("DuplicateDeclaration")
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);

        restTemplate.postForEntity(URL, sdr, ServiceDeclarationResponse.class);

        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(DUPLICATE_DECLARATION);
    }

    @Test
    void shouldNotAdd_validUntilInPast() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(Instant.EPOCH.getEpochSecond());
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_maxCacheSecondsIsNegative() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(-1)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_serviceProviderIdMissing() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(null)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_serviceDeclarationIdMissing() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(null)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_nameMissing() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(null)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_descriptionMissing() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(null)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_technicalDescriptionMissing() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(null)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_consentMaxDurationSecondsMissing() {
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(null)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

    @Test
    void shouldNotAdd_providerNotFound() {
        doReturn(ResponseEntity.notFound().build()).when(serviceProviderApiClientMock).getServiceProviderByServiceProviderId(any());
        ServiceDeclaration sdr = new ServiceDeclaration();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(null)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        ResponseEntity<ErrorResponse> responseEntity = restTemplate.postForEntity(URL, sdr, ErrorResponse.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getError()).isEqualTo(INVALID_REQUEST);
    }

}
