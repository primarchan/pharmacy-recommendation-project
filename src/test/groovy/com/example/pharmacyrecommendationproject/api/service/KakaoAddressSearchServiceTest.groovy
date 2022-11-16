package com.example.pharmacyrecommendationproject.api.service

import com.example.pharmacyrecommendationproject.AbstractIntegrationContainerBaseTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;

    def "address 파라미터 값이 null 이면, requestAddressSearch 메서드는 null 을 리턴한다."() {
        given:
        String address = null

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result == null
    }

    def "주소값이 valid 할 경우, requestAddressSearch 메서드는 정상적으로 document 를 반환한다."() {
        given:
        def address = "서울 성북구 종암로 10길"

        when:
        def result = kakaoAddressSearchService.requestAddressSearch(address)

        then:
        result.documentDtoList.size() > 0
        result.metaDto.totalCount > 0
        result.documentDtoList.get(0).addressName != null
    }

}
