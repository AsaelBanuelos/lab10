package com.example.lab10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Lab 10 - Spring Boot Security Demo.
 * This is the entry point for the Spring Boot application.
 *
 * The @SpringBootApplication annotation enables:
 * - Component scanning for Spring beans
 * - Auto-configuration based on classpath dependencies
 * - Configuration properties support
 */
@SpringBootApplication
public class Lab10Application {

    /**
     * Application entry point - starts the Spring Boot application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Lab10Application.class, args);
    }

}
