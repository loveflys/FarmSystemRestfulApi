package com.cay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FarmsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmsystemApplication.class, args);
	}
}
