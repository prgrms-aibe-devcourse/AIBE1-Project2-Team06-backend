package com.eum.ai.service.impl;

import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.ai.service.CultureFitRecommendationService;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.repository.PostRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다. ID : " + postId));

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

    private String buildPrompt (CultureFitRequest cultureFitRequest) {
        return "다음은 사용자의 컬쳐핏 성향 질문에 대한 응답입니다. 아래 JSON 응답을 바탕으로 아래 7가지 유형 중 가장 어울리는 1개만 선택해 주세요. \n\n"+
                "1. 당신은 어떤 협업 방식을 선호하나요? " + cultureFitRequest.collaborationStyle() + "\n" +
                "2. 마감일이 다가올 때 당신의 작업 스타일은 어떤가요?" + cultureFitRequest.deadlineAttitude() + "\n" +
                "3. 프로젝트 중 문제가 생기면 어떤 방식으로 해결하려 하나요?" + cultureFitRequest.problemSolvingApproach()+ "\n" +
                "4. 팀 내 소통에서 당신의 성향은?" + cultureFitRequest.communicationStyle() + "\n" +
                "5. 팀원과 의견이 다를 때 어떤 편인가요?" + cultureFitRequest.conflictResolution() + "\n" +
                "6. 당신은 어떤 방식의 업무 분배를 선호하나요?" + cultureFitRequest.tackDistribution() + "\n" +
                "가능한 컬쳐핏 유형:\n" +
                "자율형 (자기 주도 및 개인 중심), 계획형 (체계적·신중한 스타일), 소통 협업형 (협력·의사소통 중시), 실용 협업형 (효율성과 실행력 중심), 조화 중시형 (관계 중심), 지시 기반형 (명확한 체계 선호)\n\n" +
                "컬쳐핏 유형의 응답은 아래 중 하나를 골라 답해주세요.\n"+
                "자율형 : AUTONOMOUS, 계획형 : PLANNER, 소통 협업형 : COMMUNICATIVE, 실용 협업형 : PRACTICAL, 조화 중시형 : HARMONY, 지시 기반형 :DIRECTIVE\n"+
                "형식은 다음과 같이 응답해주세요:\n" +
                "{\n" +
                "   \"cultureFitType\": \"...\"\n" +
                "}";
    }
}
