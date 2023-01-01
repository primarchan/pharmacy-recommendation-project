package com.example.pharmacyrecommendationproject.domain.direction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InputDto {

    private String address;  // 입력받은 주소 (고객 입력 주소 -> 카카오 우편 서비스 API)

}
