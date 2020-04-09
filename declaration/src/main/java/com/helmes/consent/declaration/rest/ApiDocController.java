package com.helmes.consent.declaration.rest;

import com.helmes.consent.declaration.DeclarationApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@RestController
@RequestMapping("/v3")
public class ApiDocController {
    @CrossOrigin(origins = "*")
    @GetMapping("/api-doc")
    public String getApiDoc() {
        return new Scanner(DeclarationApplication.class.getResourceAsStream("/openapi.json"), StandardCharsets.UTF_8).useDelimiter("\r\n").next();
    }
}
