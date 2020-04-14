package com.helmes.consent.provider.rest;

import com.helmes.consent.provider.server.model.ServiceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProviderResourceIntegrationTest {
    private static final String URL = "/provider";

    private static final String SERVICE_PROVIDER_ID = "spId";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldFind() {
        ResponseEntity<ServiceProvider> responseEntity = restTemplate.getForEntity(URL + "/" + SERVICE_PROVIDER_ID, ServiceProvider.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getServiceProviderId()).isEqualTo(SERVICE_PROVIDER_ID);
    }

    @Test
    void shouldNotFind() {
        ResponseEntity<ServiceProvider> responseEntity = restTemplate.getForEntity(URL + "/non-existing", ServiceProvider.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
