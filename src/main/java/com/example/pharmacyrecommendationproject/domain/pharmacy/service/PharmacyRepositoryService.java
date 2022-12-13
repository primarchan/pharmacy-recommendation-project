package com.example.pharmacyrecommendationproject.domain.pharmacy.service;

import com.example.pharmacyrecommendationproject.domain.pharmacy.entity.Pharmacy;
import com.example.pharmacyrecommendationproject.domain.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class PharmacyRepositoryService {
    private final PharmacyRepository pharmacyRepository;

    // self invocation test
    public void bar(List<Pharmacy> pharmacyList) {
        log.info("bar CurrentTransactionName: " + TransactionSynchronizationManager.getCurrentTransactionName());
        foo(pharmacyList);
    }

    // self invocation test
    @Transactional
    public void foo(List<Pharmacy> pharmacyList) {
        log.info("foo CurrentTransactionName: " + TransactionSynchronizationManager.getCurrentTransactionName());
        pharmacyList.forEach(pharmacy -> {
            pharmacyRepository.save(pharmacy);
            throw new RuntimeException("error");
        });
    }

    // read only test
    @Transactional(readOnly = true)
    public void startReadOnlyMethod(Long id) {
        pharmacyRepository.findById(id).ifPresent(pharmacy -> {
            pharmacy.changePharmacyAddress("서울 특별시 광진구");
        });
    }

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
