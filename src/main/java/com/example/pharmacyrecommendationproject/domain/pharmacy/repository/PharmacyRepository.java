package com.example.pharmacyrecommendationproject.domain.pharmacy.repository;

import com.example.pharmacyrecommendationproject.domain.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

}
