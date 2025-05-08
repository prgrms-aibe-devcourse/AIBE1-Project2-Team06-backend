package com.eum.member.controller;

import com.eum.member.model.dto.request.LoginRequestDto;
import com.eum.member.model.dto.response.LoginResponseDto;
import com.eum.member.service.KakaoLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LoginController {

    private final KakaoLoginService kakaoLoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> socialLogin(@RequestBody LoginRequestDto request) {
        log.info("Login request code: {}", request.code());

        if ("kakao".equalsIgnoreCase(request.provider())) {
            LoginResponseDto response = kakaoLoginService.kakaoLogin(request.code(), request.provider());
            log.info("Login response: {}", response);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // JWT 유효성 검사 엔드포인트
    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(HttpServletRequest httpServletRequest) {
        String jwtToken = (String) httpServletRequest.getAttribute("token");

        if (kakaoLoginService.validateJwtToken(jwtToken)) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }


}
