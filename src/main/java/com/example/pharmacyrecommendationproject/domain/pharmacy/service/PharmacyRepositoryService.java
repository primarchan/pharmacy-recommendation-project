package com.example.pharmacyrecommendationproject.domain.pharmacy.service;

import com.example.pharmacyrecommendationproject.domain.pharmacy.entity.Pharmacy;
import com.example.pharmacyrecommendationproject.domain.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacyRepositoryService {
    private final PharmacyRepository pharmacyRepository;

    @Transactional
    public void updateAddress(Long id, String address) {
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error(" {PharmacyRepositoryService updateAddress} not found id: {}", id);
            return;
        }

        entity.changePharmacyAddress(address);
    }

    // for Dirty Checking test
    public void updateAddressWithOutTransaction(Long id, String address) {
        Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error(" {PharmacyRepositoryService updateAddress} not found id: {}", id);
            return;
        }

        entity.changePharmacyAddress(address);
    }

}
