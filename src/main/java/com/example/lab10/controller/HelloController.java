package com.example.lab10.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // Simple GET endpoint to test the HTTP request → response flow
    @GetMapping("/hello")
    public String hello() {

        // Returns a plain text response
        return "OK";
    }
}
