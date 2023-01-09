package com.example.pharmacyrecommendationproject.domain.pharmacy.service;

import com.example.pharmacyrecommendationproject.api.dto.DocumentDto;
import com.example.pharmacyrecommendationproject.api.dto.KakaoApiResponseDto;
import com.example.pharmacyrecommendationproject.api.service.KakaoAddressSearchService;
import com.example.pharmacyrecommendationproject.domain.direction.dto.OutputDto;
import com.example.pharmacyrecommendationproject.domain.direction.entity.Direction;
import com.example.pharmacyrecommendationproject.domain.direction.service.Base62Service;
import com.example.pharmacyrecommendationproject.domain.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;
    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;

    /**
     * @apiNote 고객으로 입력받은 주소를 기준으로 가장 가까운 약국 목록(3개) 추천
     * @param address (고객이 입력한 문자열 타입의 주소)
     */
    public List<OutputDto> recommendationPharmacyList(String address) {
        // 고객으로 부터 입력받은 문자열 타입의 주소를 Kakao API 를 이용해 위치(위도, 경도) 기반 데이터로 변환
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        // Kakao API Call Response Data Validation Check
        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtoList())) {
            log.error("[PharmacyRecommendationService recommendationPharmacyList fail] Input address: {}");
            return Collections.emptyList();
        }

        // 위치(위도, 경도) 기반 데이터로 변환된 주소
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);

        // 거리 계산 알고리즘을 통해 가장 가까운 약국 목록 조회(3개)
        List<Direction> directionList = directionService.buildDirectionList(documentDto);
        // List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto);

        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());
    }

    private OutputDto convertToOutputDto(Direction direction) {

        return OutputDto.builder()
                .pharmacyName(direction.getTargetPharmacyName())
                .pharmacyAddress(direction.getTargetAddress())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId())) // Shorten URL 로 제공
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }

}
