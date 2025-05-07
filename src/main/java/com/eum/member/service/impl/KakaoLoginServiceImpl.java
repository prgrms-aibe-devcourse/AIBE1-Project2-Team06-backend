package com.eum.member.service.impl;

import com.eum.member.auth.JwtUtil;
import com.eum.member.model.dto.response.LoginResponseDto;
import com.eum.member.model.entity.KakaoUserInfo;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.member.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class KakaoLoginServiceImpl implements KakaoLoginService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

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

    public LoginResponseDto kakaoLogin(String code, String provider) {
        String accessToken = getAccessToken(code);

        KakaoUserInfo userInfo = getUserInfo(accessToken);

        Member member = memberRepository.findMemberByAuthIdAndProvider(userInfo.getId(), provider)
                .orElseGet(() -> {
                    UUID publicId = UUID.randomUUID();

                    Member newMember = Member.of(
                            userInfo.getId(),
                            provider,
                            publicId.toString(),
                            "UNDEFINED",
                            "UNDEFINED",
                            "UNDEFINED",
                            "UNDEFINED",
                            null
                    );
                    //publicId 세팅
                    newMember.setPublicId(publicId);
                    return memberRepository.save(newMember);
                });
        String token = jwtUtil.generateToken(member.getPublicId().toString());

        return new LoginResponseDto(token);
    }

    // JWT 토큰 유효성 검사
    public boolean validateJwtToken(String token) {
        return jwtUtil.validateJwtToken(token);
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("redirect_uri", redirectUri);
        formData.add("code", code);

        Map<String, Object> response = webClient.post()
                .uri(tokenUri)
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
