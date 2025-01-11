package com.example.grocerynew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GrocerynewApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrocerynewApplication.class, args);
	}

}
