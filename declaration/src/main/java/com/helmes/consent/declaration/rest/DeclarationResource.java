package com.helmes.consent.declaration.rest;

import com.helmes.consent.declaration.server.api.DeclarationApi;
import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.server.model.ServiceDeclarationResponse;
import com.helmes.consent.declaration.service.DeclarationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class DeclarationResource implements DeclarationApi {
    private final DeclarationService declarationService;

    public DeclarationResource(DeclarationService declarationService) {
        this.declarationService = declarationService;
    }

    /**
     * Add service declaration to database. When declaration validation fails, return HTTP 400 with appropriate message
     *
     * @param serviceDeclaration (required)
     * @return ServiceDeclarationResponse on success
     */
    @Override
    public ResponseEntity<ServiceDeclarationResponse> addServiceDeclaration(@Valid ServiceDeclaration serviceDeclaration) {
        log.info("Request to save service declaration: {}", serviceDeclaration);
        ServiceDeclaration declaration = this.declarationService.save(serviceDeclaration);
        log.info("Declaration saved: {}", declaration);
        ServiceDeclarationResponse response = new ServiceDeclarationResponse();
        response.setResponse("OK");
        return ResponseEntity.ok(response);
    }
}
