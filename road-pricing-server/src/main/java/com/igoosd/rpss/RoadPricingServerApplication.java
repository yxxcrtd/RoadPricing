package com.igoosd.rpss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.igoosd")
public class RoadPricingServerApplication {

	public static void main(String[] args) {

		//Thymeleaf thymeleaf = Thymeleaf
		SpringApplication.run(RoadPricingServerApplication.class, args);
	}
}
