package com.time.swimtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SwimtimeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwimtimeApplication.class, args);
	}

}
