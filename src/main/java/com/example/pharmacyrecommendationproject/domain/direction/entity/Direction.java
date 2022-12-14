package com.example.pharmacyrecommendationproject.domain.direction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "direction")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Direction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 고객 위치 주소 데이터
    private String inputAddress;
    private double inputLatitude;
    private double inputLongitude;

    // 약국 위치 주소 데이터
    private String targetPharmacyName;
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;

    // 고객 위치 주소와 약국 위치 주소 사이의 거리
    private double distance;

}
