package com.eum.ai.service.impl;

import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.ai.service.CultureFitRecommendationService;
import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.repository.PostRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eum.ai.util.BuildPrompt.buildPrompt;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureFitRecommendationServiceImpl implements CultureFitRecommendationService {

    private final PostRepository postRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value(("${gemini.api.key}"))
    private String geminiApiKey;

    public Mono<CultureFit> recommendCultureFit(Long postId, CultureFitRequest cultureFitRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return callGeminiApi(cultureFitRequest)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(cultureFit -> {
                    post.updateCultureFit(cultureFit);
                    Post savedPost = postRepository.save(post);

                    return Mono.just(cultureFit);
                });
    }

    public Mono<CultureFit> callGeminiApi(CultureFitRequest cultureFitRequest) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> contents = new HashMap<>();
        Map<String, Object> parts = new HashMap<>();

        String prompt = buildPrompt(cultureFitRequest);
        parts.put("text", prompt);
        contents.put("parts", List.of(parts));
        requestBody.put("contents", List.of(contents));

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("generativelanguage.googleapis.com")
                        .path(geminiApiUrl)
                        .queryParam("key", geminiApiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(
                                        new RuntimeException("API 호출 실패: " +response.statusCode() + " - " +errorBody ))))
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    try {
                        JsonNode rootNode = objectMapper.readTree(responseBody);
                        JsonNode textNode = rootNode
                                .path("candidates").get(0)
                                .path("content").path("parts").get(0)
                                .path("text");

                        String rawText = textNode.asText();
                        log.info("Gemini 응답 원본 : {}", rawText);

                        // 응답 형식 : "```json\n{\n   \"cultureFitType\": \"COMMUNICATIVE\"\n}\n```\n"
                        String jsonPart = rawText.replaceAll("(?s)```json\\s*", "") // ```json\n 제거
                                .replaceAll("(?s)```\\s*", "")   // ```제거
                                .trim();

                        JsonNode parsedJson = objectMapper.readTree(jsonPart);
                        String cultureFitStr = parsedJson.path("cultureFitType").asText().trim();

                        return Mono.just(CultureFit.valueOf(cultureFitStr));
                    } catch (Exception e) {
                        log.error("Gemini API 응답 파싱 중 오류 발생", e);
                        return Mono.error(new RuntimeException("Gemini API 응답 파싱 중 오류가 발생했습니다: " + e.getMessage(), e));
                    }
                });
    }

}
