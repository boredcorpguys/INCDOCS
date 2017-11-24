package com.incdocs.incdocsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class IncdocsServerApplication {
	@RequestMapping("/")
	public String home() {
		return "Hello World!";
	}

	@RequestMapping("/_ah/health")
	public String healthy() {
		// Message body required though ignored
		return "Still surviving.";
	}
	public static void main(String[] args) {
		SpringApplication.run(IncdocsServerApplication.class, args);
	}
}