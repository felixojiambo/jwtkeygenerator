package com.emtech.JWTauth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class JwTauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwTauthApplication.class, args);
		System.out.println("Server started on port 8000");
	}



}
