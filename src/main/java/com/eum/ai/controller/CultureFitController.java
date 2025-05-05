package com.eum.ai.controller;

import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.ai.service.CultureFitRecommendationService;
import com.eum.post.model.entity.enumerated.CultureFit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/culture-fit")
public class CultureFitController {

    private final CultureFitRecommendationService cultureFitService;

    @PostMapping("/{postId}/recommend")
    public Mono<CultureFit> recommendCultureFit(
            @PathVariable Long postId,
            @RequestBody CultureFitRequest request) {
        return cultureFitService.recomendCultureFit(postId, request);
    }
}
