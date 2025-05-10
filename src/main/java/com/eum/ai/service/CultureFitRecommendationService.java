package com.eum.ai.service;

import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.post.model.entity.enumerated.CultureFit;
import reactor.core.publisher.Mono;

public interface CultureFitRecommendationService {
    Mono<CultureFit> recommendCultureFit(Long postId, CultureFitRequest cultureFitRequest);
    Mono<CultureFit> previewCultureFit(CultureFitRequest cultureFitRequest);
}
