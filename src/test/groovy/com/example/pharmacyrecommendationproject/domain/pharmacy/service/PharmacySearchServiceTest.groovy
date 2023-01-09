package com.example.pharmacyrecommendationproject.domain.pharmacy.service

import com.example.pharmacyrecommendationproject.domain.pharmacy.cache.PharmacyRedisTemplateService
import com.example.pharmacyrecommendationproject.domain.pharmacy.entity.Pharmacy
import org.assertj.core.util.Lists
import spock.lang.Specification

class PharmacySearchServiceTest extends Specification {

    private PharmacySearchService pharmacySearchService
    private PharmacyRepositoryService pharmacyRepositoryService = Mock()
    private PharmacyRedisTemplateService pharmacyRedisTemplateService = Mock()

    private List<Pharmacy> pharmacyList

    def setup() {
        pharmacySearchService = new PharmacySearchService(pharmacyRepositoryService, pharmacyRedisTemplateService)

        pharmacyList = Lists.newArrayList(
                Pharmacy.builder()
                        .id(1L)
                        .pharmacyName("호수온누리약국")
                        .latitude(37.60894036)
                        .longitude(127.029052)
                        .build(),
                Pharmacy.builder()
                        .id(2L)
                        .pharmacyName("들곶이온누리약국")
                        .latitude(37.61040424)
                        .longitude(127.0569046)
                        .build()
        )
    }


    def "Redis 장애 시 DB 를 이용해 약국 데이터 조회"() {
        when:
        pharmacyRedisTemplateService.findAll() >> []
        pharmacyRepositoryService.findAll() >> pharmacyList

        def result = pharmacySearchService.searchPharmacyDtoList()

        then:
        result.size() == 2
    }

}
