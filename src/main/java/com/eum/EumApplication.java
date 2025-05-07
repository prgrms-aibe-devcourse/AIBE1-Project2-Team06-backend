package com.eum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EumApplication {

	public static void main(String[] args) {
		SpringApplication.run(EumApplication.class, args);
	}
}
