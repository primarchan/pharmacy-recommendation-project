package com.example.pharmacyrecommendationproject.api.service;

import com.example.pharmacyrecommendationproject.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("{kakao.rest.api.ket}")
    private String kakaoRestApiKey;

    public KakaoApiResponseDto requestAddressSearch(String address) {

        // 1. validation check
        if (ObjectUtils.isEmpty(address)) {
            return null;
        }

        // 2. URI 생성
        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);

        // 3. Header 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();
    }

}
