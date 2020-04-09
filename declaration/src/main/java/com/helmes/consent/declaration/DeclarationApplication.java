package com.helmes.consent.declaration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DeclarationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeclarationApplication.class, args);
    }

}
