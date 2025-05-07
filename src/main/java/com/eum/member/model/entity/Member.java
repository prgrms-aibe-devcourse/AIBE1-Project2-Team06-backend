package com.eum.member.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "Member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private UUID publicId; // 외부 통신에 쓰이는 id -> jwt 만들기

    // 카카오에서 받은 user 식별자
    @Column(nullable = false, unique = true)
    private String authId;
    @Column(nullable = false)
    private String provider;

    // 프로필에서 설정할 정보
    @Column(nullable = false, unique = true)
    private String nickname;
    @Column
    private String profileImageUrl;
    // 직무
    @Column(nullable = false)
    private String position;
    // 기술 스택
    @Column(nullable = false)
    private String techStack;
    // 경력
    @Column(nullable = false)
    private String career;
    @Column
    private String shortDescription;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    public static Member of (
            String authId,
            String provider,
            String nickname,
            String profileImageUrl,
            String position,
            String techStack,
            String career,
            String shortDescription
    ) {
        Member member = new Member();
        member.authId = authId;
        member.provider = provider;
        member.nickname = nickname;
        member.profileImageUrl = profileImageUrl;
        member.position = position;
        member.techStack = techStack;
        member.career = career;
        member.shortDescription = shortDescription;

        return member;
    }

}
