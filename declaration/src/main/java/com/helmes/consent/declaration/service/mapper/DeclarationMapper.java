package com.helmes.consent.declaration.service.mapper;

import com.helmes.consent.declaration.domain.Declaration;
import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeclarationMapper extends EntityMapper<ServiceDeclaration, Declaration> {
    default Declaration fromId(Long id) {
        if (id == null) {
            return null;
        }
        Declaration entity = new Declaration();
        entity.setId(id);
        return entity;
    }
}
