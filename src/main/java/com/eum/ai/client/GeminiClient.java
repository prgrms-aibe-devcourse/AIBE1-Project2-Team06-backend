package com.eum.ai.client;

import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eum.ai.util.BuildPrompt.buildPrompt;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public Mono<CultureFit> requestCultureFit(CultureFitRequest cultureFitRequest) {
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
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(
                                        new RuntimeException("Gemini API 호출 실패: " + response.statusCode() + " - " + errorBody)))
                )
                .bodyToMono(String.class)
                .flatMap(this::parseCultureFitFromResponse);
    }

    private Mono<CultureFit> parseCultureFitFromResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode textNode = rootNode
                    .path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text");

            String rawText = textNode.asText();
            log.info("Gemini 응답 원본 : {}", rawText);

            String jsonPart = rawText.replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```\\s*", "")
                    .trim();

            JsonNode parsedJson = objectMapper.readTree(jsonPart);
            String cultureFitStr = parsedJson.path("cultureFitType").asText().trim();

            return Mono.just(CultureFit.valueOf(cultureFitStr));
        } catch (Exception e) {
            log.error("Gemini 응답 파싱 오류", e);
            return Mono.error(new RuntimeException("응답 파싱 오류: " + e.getMessage(), e));
        }
    }
}
