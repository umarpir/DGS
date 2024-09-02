package com.dgs.dgs_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DgsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DgsBackendApplication.class, args);
	}


}
