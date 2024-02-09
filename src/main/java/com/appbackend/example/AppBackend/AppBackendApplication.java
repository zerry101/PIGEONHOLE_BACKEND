package com.appbackend.example.AppBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EntityScan

@EnableWebSecurity
public class AppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppBackendApplication.class, args);
    }







}




