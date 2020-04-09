package com.helmes.consent.provider.service.mapper;

import com.helmes.consent.provider.domain.Provider;
import com.helmes.consent.provider.server.model.ServiceProvider;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProviderMapper extends EntityMapper<ServiceProvider, Provider> {
    default Provider fromId(Long id) {
        if (id == null) {
            return null;
        }
        Provider entity = new Provider();
        entity.setId(id);
        return entity;
    }
}
