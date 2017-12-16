package com.incdocs.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ImportResource("classpath:queries.xml")
public class IncdocsServerApplication {


    public static void main(String[] args) {

        SpringApplication.run(IncdocsServerApplication.class, args);
    }
}