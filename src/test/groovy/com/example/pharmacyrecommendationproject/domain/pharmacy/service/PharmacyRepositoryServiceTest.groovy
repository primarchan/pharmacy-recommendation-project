package com.example.pharmacyrecommendationproject.domain.pharmacy.service

import com.example.pharmacyrecommendationproject.AbstractIntegrationContainerBaseTest
import com.example.pharmacyrecommendationproject.domain.pharmacy.entity.Pharmacy
import com.example.pharmacyrecommendationproject.domain.pharmacy.repository.PharmacyRepository
import org.springframework.beans.factory.annotation.Autowired

class PharmacyRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PharmacyRepositoryService pharmacyRepositoryService;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    /**
     * 테스트 메서드가 종료될 때마다 DB 의 데이터를 삭제
     */
    def setup() {
        pharmacyRepository.deleteAll();
    }

    def "PharmacyRepository update - dirty checking success"() {
        given:
        String inputAddress = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .build()

        when:
        def entity = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getPharmacyAddress() == modifiedAddress  // 저장된 주소가 변경된 주소인지 체크
    }

    def "PharmacyRepository update - dirty checking fail"() {
        given:
        String inputAddress = "서울 특별시 성북구 종암동"
        String modifiedAddress = "서울 광진구 구의동"
        String name = "은혜 약국"

        def pharmacy = Pharmacy.builder()
                .pharmacyAddress(inputAddress)
                .pharmacyName(name)
                .build()

        when:
        def entity = pharmacyRepository.save(pharmacy)
        pharmacyRepositoryService.updateAddressWithOutTransaction(entity.getId(), modifiedAddress)

        def result = pharmacyRepository.findAll()

        then:
        result.get(0).getPharmacyAddress() == inputAddress  // 주소가 변경되지 않고 원래 주소인지 체크
    }

}
