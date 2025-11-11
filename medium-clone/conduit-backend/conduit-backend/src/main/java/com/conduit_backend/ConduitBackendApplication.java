package com.conduit_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.conduit_backend")
public class ConduitBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConduitBackendApplication.class, args);
	}

}
