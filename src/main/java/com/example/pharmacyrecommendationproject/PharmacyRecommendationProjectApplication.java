package com.example.pharmacyrecommendationproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PharmacyRecommendationProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PharmacyRecommendationProjectApplication.class, args);
    }

}
