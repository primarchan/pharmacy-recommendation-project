package com.example.pharmacyrecommendationproject.domain.pharmacy.service;

import com.example.pharmacyrecommendationproject.domain.pharmacy.cache.PharmacyRedisTemplateService;
import com.example.pharmacyrecommendationproject.domain.pharmacy.dto.PharmacyDto;
import com.example.pharmacyrecommendationproject.domain.pharmacy.entity.Pharmacy;
import com.example.pharmacyrecommendationproject.domain.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacySearchService {

    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;

    public List<PharmacyDto> searchPharmacyDtoList() {

        // Redis
        List<PharmacyDto> pharmacyDtoList = pharmacyRedisTemplateService.findAll();

        if (!pharmacyDtoList.isEmpty()) {
            return pharmacyDtoList;
        }

        // DB
        return pharmacyRepositoryService.findAll()
                .stream()
                .map(this::convertToPharmacyDto)
                .collect(Collectors.toList());
    }

    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy) {
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }

}
