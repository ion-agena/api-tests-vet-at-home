package com.agenatech.test.vetathome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableFeignClients
//@EnableHypermediaSupport(type = {})
public class VetAtHomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetAtHomeApplication.class, args);
	}

}
