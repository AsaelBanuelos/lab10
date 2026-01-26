package com.example.lab10.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/*
 * Simple public REST controller.
 * Used mainly for testing and HTTP demonstrations.
 */
@RestController
public class HelloController {

    /*
     * Simple endpoint to check if the app is running.
     * If this returns "OK", the server is up.
     */
    @GetMapping("/hello")
    public String hello() {
        return "OK";
    }

    /*
     * Endpoint that reads HTTP request headers.
     * Useful to show how headers work in Spring.
     */
    @GetMapping("/headers")
    public String headers(

            // Gets the User-Agent header from the request
            @RequestHeader(value = "User-Agent", required = false) String userAgent,

            // Gets the Accept header from the request
            @RequestHeader(value = "Accept", required = false) String accept
    ) {

        // Return the header values as plain text
        return "User-Agent: " + userAgent + "\n" +
                "Accept: " + accept;
    }
}