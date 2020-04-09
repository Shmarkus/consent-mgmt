package com.helmes.consent.declaration.rest;

import com.helmes.consent.declaration.server.model.ServiceDeclaration;
import com.helmes.consent.declaration.service.DeclarationService;
import com.helmes.consent.declaration.service.impl.DuplicateDeclarationException;
import com.helmes.consent.declaration.service.validation.DeclarationValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeclarationResourceTest {
    @Mock
    private DeclarationService mockDeclarationService;

    private MockMvc restServiceMockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        doReturn(new ServiceDeclaration()).when(mockDeclarationService).save(any());
        DeclarationResource declarationResource = new DeclarationResource(mockDeclarationService);
        this.restServiceMockMvc = MockMvcBuilders.standaloneSetup(declarationResource).build();
    }

    @Test
    void shouldAddValidServiceDeclaration() throws Exception {
        restServiceMockMvc.perform(post("/declaration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"serviceProviderId\": \"string\",\n" +
                        "  \"serviceDeclarationId\": \"string\",\n" +
                        "  \"name\": \"string\",\n" +
                        "  \"description\": \"string\",\n" +
                        "  \"technicalDescription\": \"string\",\n" +
                        "  \"consentMaxDurationSeconds\": 0,\n" +
                        "  \"needSignature\": false,\n" +
                        "  \"validUntil\": " + Instant.now().plusSeconds(60).getEpochSecond() + ",\n" +
                        "  \"maxCacheSeconds\": 0\n" +
                        "}")
        ).andExpect(status().isOk())
                .andExpect(content().bytes("{\"response\":\"OK\"}".getBytes()));
    }

    @Test
    void shouldNotAddServiceDeclaration_serviceThrowsValidationError() throws Exception {
        doThrow(DeclarationValidationException.class).when(mockDeclarationService).save(any());
        restServiceMockMvc.perform(post("/declaration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"serviceProviderId\": \"string\",\n" +
                        "  \"serviceDeclarationId\": \"string\",\n" +
                        "  \"name\": \"string\",\n" +
                        "  \"description\": \"string\",\n" +
                        "  \"technicalDescription\": \"string\",\n" +
                        "  \"consentMaxDurationSeconds\": 0,\n" +
                        "  \"needSignature\": false,\n" +
                        "  \"validUntil\": " + Instant.now().plusSeconds(60).getEpochSecond() + ",\n" +
                        "  \"maxCacheSeconds\": 0\n" +
                        "}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    void shouldNotAddServiceDeclaration_serviceThrowsDuplicateDataError() throws Exception {
        doThrow(DuplicateDeclarationException.class).when(mockDeclarationService).save(any());
        restServiceMockMvc.perform(post("/declaration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"serviceProviderId\": \"string\",\n" +
                        "  \"serviceDeclarationId\": \"string\",\n" +
                        "  \"name\": \"string\",\n" +
                        "  \"description\": \"string\",\n" +
                        "  \"technicalDescription\": \"string\",\n" +
                        "  \"consentMaxDurationSeconds\": 0,\n" +
                        "  \"needSignature\": false,\n" +
                        "  \"validUntil\": " + Instant.now().plusSeconds(60).getEpochSecond() + ",\n" +
                        "  \"maxCacheSeconds\": 0\n" +
                        "}")
        ).andExpect(status().is4xxClientError());
    }
}
