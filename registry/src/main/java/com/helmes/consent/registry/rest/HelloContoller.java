package com.helmes.consent.registry.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloContoller {
    @GetMapping(path = "/")
    public String getWorld() {
        return "World";
    }
}
