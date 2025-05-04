package com.eum.member.controller;

import com.eum.member.model.dto.request.LoginRequestDto;
import com.eum.member.model.dto.response.LoginResponseDto;
import com.eum.member.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final KakaoLoginService kakaoLoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> socialLogin(@RequestBody LoginRequestDto request) {
        log.info("Login request: {}", request.code());

        if ("kakao".equalsIgnoreCase(request.provider())) {
            LoginResponseDto response = kakaoLoginService.kakaoLogin(request.code());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
