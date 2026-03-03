package com.smartparking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartParkingSystemApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SmartParkingSystemApplication.class);
		// Ensure H2 console auto-configuration is not excluded
		application.run(args);
	}

}
