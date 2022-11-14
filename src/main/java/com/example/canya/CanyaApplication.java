package com.example.canya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CanyaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CanyaApplication.class, args);
    }

}
