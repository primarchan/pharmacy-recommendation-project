package com.example.pharmacyrecommendationproject.domain.direction.controller;

import com.example.pharmacyrecommendationproject.domain.direction.entity.Direction;
import com.example.pharmacyrecommendationproject.domain.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;

    @GetMapping("/dir/{encodedId}")
    public String searchDirection(@PathVariable("encodedId") String encodedId) {
        String result = directionService.findDirectionUrlById(encodedId);
        log.info("[DirectionController searchDirection] direction URL: {}", result);

        return "redirect:" + result;
    }

}
