package com.eum.ai.service.impl;

import com.eum.ai.client.GeminiClient;
import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.ai.service.CultureFitRecommendationService;
import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureFitRecommendationServiceImpl implements CultureFitRecommendationService {

    private final PostRepository postRepository;
    private final GeminiClient geminiClient;

    public Mono<CultureFit> recommendCultureFit(Long postId, CultureFitRequest cultureFitRequest) {
        return Mono.defer(() ->{
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

            return geminiClient.requestCultureFit(cultureFitRequest)
                    .publishOn(Schedulers.boundedElastic())
                    .flatMap(cultureFit -> {
                        post.updateCultureFit(cultureFit);
                        postRepository.save(post);
                        return Mono.just(cultureFit);
                    });
        });
    }

    @Override
    public Mono<CultureFit> previewCultureFit(CultureFitRequest cultureFitRequest) {
        return geminiClient.requestCultureFit(cultureFitRequest);
    }

}
