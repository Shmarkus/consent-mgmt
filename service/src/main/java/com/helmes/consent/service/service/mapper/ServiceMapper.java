package com.helmes.consent.service.service.mapper;

import com.helmes.consent.service.domain.Service;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper extends EntityMapper<ServiceDeclarationRequest, Service> {
    default Service fromId(Long id) {
        if (id == null) {
            return null;
        }
        Service entity = new Service();
        entity.setId(id);
        return entity;
    }
}
