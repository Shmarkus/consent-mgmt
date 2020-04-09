package com.helmes.consent.declaration.service.validation;

import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class DeclarationDeclarationRequestValidatorImplTest {
    final String SERVICE_DECLARATION_ID = "serviceDeclarationId";
    final String SERVICE_PROVIDER_ID = "serviceProviderId";
    final int CONSENT_MAX_DURATION = 0;
    final int MAX_CACHE = 0;
    final String DESCRIPTION = "description";
    final String NAME = "name";
    final boolean DOES_NOT_NEED_SIG = false;
    final String TECHNICAL_DESCRIPTION = "E";
    final long VALID_UNTIL_FUTURE = Instant.now().plusSeconds(60 * 60).getEpochSecond();

    private final DeclarationRequestValidator declarationRequestValidator = new DeclarationRequestValidatorImpl();

    @Test
    void shouldNotBeValid_validUntilInPast() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_maxCacheSecondsIsNegative() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_serviceProviderIdMissing() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_serviceDeclarationIdMissing() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_nameMissing() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_descriptionMissing() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_technicalDescriptionMissing() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldNotBeValid_consentMaxDurationSecondsMissing() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isFalse();
    }

    @Test
    void shouldBeValid() {
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
        final boolean isValid = declarationRequestValidator.isValid(sdr);
        assertThat(isValid).isTrue();
    }
}
