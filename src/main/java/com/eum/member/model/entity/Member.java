package com.eum.member.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "Member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
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
    // 경력
    @Column(nullable = false)
    private String career;
    @Column
    private String shortDescription;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberPosition> memberPositions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberTechStack> memberTechStacks = new ArrayList<>();

    public static Member of (
            String authId,
            String provider,
            String profileImageUrl,
            String career,
            String shortDescription
    ) {
        Member member = new Member();
        member.publicId = UUID.randomUUID();
        member.authId = authId;
        member.provider = provider;
        member.nickname = member.publicId.toString();
        member.profileImageUrl = profileImageUrl;
        member.career = career;
        member.shortDescription = shortDescription;

        return member;
    }

    public void addPosition(MemberPosition memberPosition) {
        memberPositions.add(memberPosition);
    }

    public void addTechStack(MemberTechStack memberTechStack) {
        memberTechStacks.add(memberTechStack);
    }

    public void updateProfile(String nickname, String career, String shortDescription) {
        this.nickname = nickname;
        this.career = career;
        this.shortDescription = shortDescription;
    }
}
