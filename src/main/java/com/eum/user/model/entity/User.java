package com.eum.user.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오에서 받은 user 식별자
    @Column(nullable = false, unique = true)
    private String kakaoId;
    // 닉네임
    @Column(nullable = false)
    private String nickname;

    // 프로필에서 설정할 정보
    private String profileImageUrl;
    // 직무
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private String career;
    private String shortDescription;

    @CreationTimestamp
    ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC);
}
