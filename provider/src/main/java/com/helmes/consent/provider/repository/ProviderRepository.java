package com.helmes.consent.provider.repository;

import com.helmes.consent.provider.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    Optional<Provider> findOneByServiceProviderId(String serviceProviderId);
}
