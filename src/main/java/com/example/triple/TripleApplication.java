package com.example.triple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TripleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripleApplication.class, args);
    }

}
