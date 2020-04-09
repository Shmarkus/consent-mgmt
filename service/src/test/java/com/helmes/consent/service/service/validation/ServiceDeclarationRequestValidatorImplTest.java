package com.helmes.consent.service.service.validation;

import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ServiceDeclarationRequestValidatorImplTest {
    final String SERVICE_DECLARATION_ID = "serviceDeclarationId";
    final String SERVICE_PROVIDER_ID = "serviceProviderId";
    final int CONSENT_MAX_DURATION = 0;
    final int MAX_CACHE = 0;
    final String DESCRIPTION = "description";
    final String NAME = "name";
    final boolean DOES_NOT_NEED_SIG = false;
    final String TECHNICAL_DESCRIPTION = "E";
    final long VALID_UNTIL_FUTURE = Instant.now().plusSeconds(60 * 60).getEpochSecond();

    private final ServiceDeclarationRequestValidator serviceDeclarationRequestValidator = new ServiceDeclarationRequestValidatorImpl();

    @Test
    void shouldNotBeValid_validUntilInPast() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(Instant.EPOCH.getEpochSecond());
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_maxCacheSecondsIsNegative() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(-1)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_serviceProviderIdMissing() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(null)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_serviceDeclarationIdMissing() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(null)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_nameMissing() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(null)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_descriptionMissing() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(null)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_technicalDescriptionMissing() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(CONSENT_MAX_DURATION)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(null)
                .validUntil(VALID_UNTIL_FUTURE);
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_consentMaxDurationSecondsMissing() {
        ServiceDeclarationRequest sdr = new ServiceDeclarationRequest();
        sdr.serviceDeclarationId(SERVICE_DECLARATION_ID)
                .serviceProviderId(SERVICE_PROVIDER_ID)
                .consentMaxDurationSeconds(null)
                .maxCacheSeconds(MAX_CACHE)
                .description(DESCRIPTION)
                .name(NAME)
                .needSignature(DOES_NOT_NEED_SIG)
                .technicalDescription(TECHNICAL_DESCRIPTION)
                .validUntil(VALID_UNTIL_FUTURE);
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldBeValid() {
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
        final boolean isValid = serviceDeclarationRequestValidator.isValid(sdr);
        assertThat(isValid).isTrue();
    }
}
