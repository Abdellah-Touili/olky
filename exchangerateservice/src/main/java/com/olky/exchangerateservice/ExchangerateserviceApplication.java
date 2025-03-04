package com.olky.exchangerateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EntityScan(basePackages="com.olky.exchangerateservice")
public class ExchangerateserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExchangerateserviceApplication.class, args);
	}
}
