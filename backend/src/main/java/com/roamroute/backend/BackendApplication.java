package com.roamroute.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Spring Boot application entry point for the RoamRoute backend.
 *
 * Contains the `main` method which boots the Spring application context.
 */
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
