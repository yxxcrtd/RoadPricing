package com.igoosd.rps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.igoosd")
public class RoadPricingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoadPricingSystemApplication.class, args);
	}
}
