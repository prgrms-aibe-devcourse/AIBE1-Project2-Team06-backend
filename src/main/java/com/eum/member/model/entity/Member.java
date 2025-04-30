package com.eum.member.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

//    private UUID publicId;

    // 카카오에서 받은 user 식별자
    @Column(nullable = false, unique = true)
    private String authId;
    // 닉네임
    @Column(nullable = false)
    private String nickname;

    // 프로필에서 설정할 정보
    @Column
    private String profileImageUrl;
    // 직무
    @Column(nullable = false)
    private String position;
    @Column(nullable = false)
    private String career;
    @Column
    private String shortDescription;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    public static Member of (
            String nickname,
            String profileImageUrl,
            String position,
            String career,
            String shortDescription
    ) {
        Member member = new Member();
        member.nickname = nickname;
        member.profileImageUrl = profileImageUrl;
        member.position = position;
        member.career = career;
        member.shortDescription = shortDescription;

        return member;
    }


}
