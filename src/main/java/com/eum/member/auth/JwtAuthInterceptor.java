package com.eum.member.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");

            if (jwtUtil.validateJwtToken(token)) {
                Claims claims = jwtUtil.extractClaims(token);
                UUID publicId = UUID.fromString(claims.getSubject());

                // 요청 속성에 저장
                request.setAttribute("publicId", publicId);
                return true;
            }
        }

        // 토큰이 없거나 유효하지 않은 경우 요청 차단
        // 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Invalid or missing JWT token\"}");
        return false;
    }
}
