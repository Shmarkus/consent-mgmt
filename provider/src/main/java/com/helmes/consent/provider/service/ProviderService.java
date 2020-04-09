package com.helmes.consent.provider.service;

import com.helmes.consent.provider.server.model.ServiceProvider;

import java.util.Optional;

public interface ProviderService {
    /**
     * Use service provider ID to find a provider
     * @param serviceProviderId Service Provider ID (not internal ID)
     * @return Provider
     */
    Optional<ServiceProvider> findByServiceProviderID(String serviceProviderId);
}
