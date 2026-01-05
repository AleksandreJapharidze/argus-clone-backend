package com.example.argusclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArgusCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArgusCloneApplication.class, args);
	}

}
