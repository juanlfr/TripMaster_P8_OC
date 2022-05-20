package com.tripmaster.microservice.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		System.out.println("*************************" + Locale.getDefault() + "************************************");
		SpringApplication.run(Application.class, args);
	}

}
