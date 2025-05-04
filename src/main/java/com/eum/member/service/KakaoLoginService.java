package com.eum.member.service;

import com.eum.member.model.dto.response.LoginResponseDto;
import com.eum.member.model.entity.KakaoUserInfo;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class KakaoLoginService {
    private final MemberRepository memberRepository;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-uri}")
    private String tokenUri;

    @Value("${kakao.user-info-uri}")
    private String userInfoUri;

    private final WebClient webClient = WebClient.create();

    public LoginResponseDto kakaoLogin(String code) {
        String accessToken = getAccessToken(code);

        KakaoUserInfo userInfo = getUserInfo(accessToken);

        Member member = memberRepository.findMemberByAuthId(userInfo.getId())
                .orElseGet(() -> {
                    Member newMember = Member.of(
                            userInfo.getId(),
                            userInfo.getNickname(),
                            "UNDEFINED",
                            "UNDEFINED",
                            "UNDEFINED",
                            null
                    );
                    return memberRepository.save(newMember);
                });
        return new LoginResponseDto(member.getId(), member.getAuthId(), member.getNickname());
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);

        Map<String, Object> response = webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        log.info("Access Token Response: {}", response);
        return (String) response.get("access_token");
    }

    private KakaoUserInfo getUserInfo(String accessToken) {
        Map<String, Object> response = webClient.get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        log.info("Kakao User Info: {}", response);

        String id = String.valueOf(response.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new KakaoUserInfo(
                id,
                (String) profile.get("nickname")
        );
    }
}
