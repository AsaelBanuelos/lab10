package com.example.lab10.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for testing and demonstration endpoints.
 * These endpoints are publicly accessible and used for basic HTTP testing.
 */
@RestController
public class HelloController {

    /**
     * Simple health check endpoint.
     * Returns "OK" to verify the application is running.
     */
    @GetMapping("/hello")
    public String hello() {
        return "OK";
    }

    /**
     * Demonstration endpoint showing how to read HTTP request headers.
     * Extracts and displays common headers from the incoming request.
     */
    @GetMapping("/headers")
    public String headers(
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = "Accept", required = false) String accept
    ) {
        return "User-Agent: " + userAgent + "\n" +
                "Accept: " + accept;
    }
}
