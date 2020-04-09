package com.helmes.consent.provider.rest;

import com.helmes.consent.provider.server.api.ProviderApi;
import com.helmes.consent.provider.server.model.ServiceProvider;
import com.helmes.consent.provider.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class ProviderResource implements ProviderApi {
    private final ProviderService providerService;

    public ProviderResource(ProviderService providerService) {
        this.providerService = providerService;
    }

    @Override
    public ResponseEntity<ServiceProvider> getServiceProviderByServiceProviderId(String providerId) {
        log.debug("Request to get service provider by ID: {}", providerService);
        Optional<ServiceProvider> byServiceProviderID = providerService.findByServiceProviderID(providerId);
        return byServiceProviderID.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
