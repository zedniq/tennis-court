package com.IQproject.court;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Tennis Court Reservation System application.
 *
 * @author Vojtech Zednik
 */
@SpringBootApplication
public class CourtApplication {

    /**
     * Main method that launches the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CourtApplication.class, args);
    }
}
