package com.helmes.consent.provider.service.impl;

import com.helmes.consent.provider.repository.ProviderRepository;
import com.helmes.consent.provider.server.model.ServiceProvider;
import com.helmes.consent.provider.service.ProviderService;
import com.helmes.consent.provider.service.mapper.ProviderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    public ProviderServiceImpl(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
    }

    @Override
    public Optional<ServiceProvider> findByServiceProviderID(String serviceProviderId) {
        log.debug("Find service provider by service provider ID: {}", serviceProviderId);
        return providerRepository.findOneByServiceProviderId(serviceProviderId)
                .map(providerMapper::toDto);
    }
}
