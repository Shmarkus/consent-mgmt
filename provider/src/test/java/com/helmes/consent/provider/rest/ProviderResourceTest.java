package com.helmes.consent.provider.rest;

import com.helmes.consent.provider.server.model.ServiceProvider;
import com.helmes.consent.provider.service.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProviderResourceTest {
    static final long ID = 1L;
    static final String NAME = "Test Provider";
    static final String SERVICE_PROVIDER_ID = "spId";
    static final String REGISTRY_NUMBER = "1234";
    @Mock
    private ProviderService mockProviderService;
    private MockMvc restServiceMockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ServiceProvider sp = new ServiceProvider();
        sp.id(ID).name(NAME).serviceProviderId(SERVICE_PROVIDER_ID).registryNumber(REGISTRY_NUMBER);
        doReturn(Optional.of(sp)).when(mockProviderService).findByServiceProviderID(any());
        ProviderResource providerResource = new ProviderResource(mockProviderService);
        this.restServiceMockMvc = MockMvcBuilders.standaloneSetup(providerResource).build();
    }

    @Test
    void shouldFindServiceProvider() throws Exception {
        String expected = "{\"id\":" + ID + ",\"name\":\"" + NAME + "\",\"serviceProviderId\":\"" + SERVICE_PROVIDER_ID + "\",\"registryNumber\":\"" + REGISTRY_NUMBER + "\"}";
        restServiceMockMvc.perform(get("/provider/spId")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().bytes(expected.getBytes()));
    }

    @Test
    void shouldNotFindServiceProvider() throws Exception {
        doReturn(Optional.empty()).when(mockProviderService).findByServiceProviderID(any());
        restServiceMockMvc.perform(get("/provider/spId-1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}
