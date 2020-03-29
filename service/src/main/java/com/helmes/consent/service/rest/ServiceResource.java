package com.helmes.consent.service.rest;

import com.helmes.consent.service.service.validation.ServiceValidationException;
import com.helmes.consent.service.server.api.ServiceApi;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.server.model.ServiceDeclarationResponse;
import com.helmes.consent.service.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Slf4j
@RestController
public class ServiceResource implements ServiceApi {
    private final ServiceService serviceService;

    public ServiceResource(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Override
    public ResponseEntity<ServiceDeclarationResponse> addServiceDeclaration(@Valid ServiceDeclarationRequest serviceDeclarationRequest) {
        log.debug("Request to save service declaration: {}", serviceDeclarationRequest);
        try {
            return ResponseEntity.ok(this.serviceService.save(serviceDeclarationRequest));
        } catch (ServiceValidationException sve) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sve.getMessage(), sve);
        }
    }
}
