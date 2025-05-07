package com.eum.member.service;

import com.eum.member.model.dto.response.LoginResponseDto;


public interface KakaoLoginService {
    LoginResponseDto kakaoLogin(String code, String provider);

    boolean validateJwtToken(String jwtToken);
}
