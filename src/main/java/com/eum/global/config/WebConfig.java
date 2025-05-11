package com.eum.global.config;

import com.eum.member.auth.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://aibe-eum.store")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/v1/members/profile")
                .addPathPatterns("/api/v1/validate-token")
                .addPathPatterns("/api/v1/members/profile/me") // 필요한 경로 지정
                .addPathPatterns("/api/v1/posts/**")
                .addPathPatterns("/api/v1/peer-reviews/**")
                .addPathPatterns("/api/v1/culture-fit/**"); // 필요한 경로 지정
    }
}
