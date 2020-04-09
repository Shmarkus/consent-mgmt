package com.helmes.consent.service.rest;

import com.helmes.consent.service.server.api.ServiceApi;
import com.helmes.consent.service.server.model.ServiceDeclarationRequest;
import com.helmes.consent.service.server.model.ServiceDeclarationResponse;
import com.helmes.consent.service.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class ServiceResource implements ServiceApi {
    private final ServiceService serviceService;

    public ServiceResource(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Add service declaration to database. When declaration validation fails, return HTTP 400 with appropriate message
     *
     * @param serviceDeclarationRequest (required)
     * @return ServiceDeclarationResponse on success
     */
    @Override
    public ResponseEntity<ServiceDeclarationResponse> addServiceDeclaration(@Valid ServiceDeclarationRequest serviceDeclarationRequest) {
        log.debug("Request to save service declaration: {}", serviceDeclarationRequest);
        this.serviceService.save(serviceDeclarationRequest);
        ServiceDeclarationResponse response = new ServiceDeclarationResponse();
        response.setResponse("OK");
        return ResponseEntity.ok(response);
    }
}
