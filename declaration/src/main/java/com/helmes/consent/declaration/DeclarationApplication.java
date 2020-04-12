package com.helmes.consent.declaration;

import com.helmes.consent.declaration.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@EnableConfigurationProperties({ApplicationProperties.class})
@SpringBootApplication
public class DeclarationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeclarationApplication.class, args);
    }

}
