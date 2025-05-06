package com.eum.post.model.entity;

import com.eum.post.model.dto.PostUpdateDto;
import com.eum.post.model.entity.enumerated.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "Post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시물 ID

    @Column(nullable = false)
    private Long userId; // 모집자 ID

    @Column(nullable = false , length = 255)
    private String title; // 게시물 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시물 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitType recruitType; // 게시물이 스터디, 프로젝트

    @Column(nullable = false)
    private Integer recruitMember; // 모집 인원

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressMethod progressMethod; // 진행 방식 (온라인, 오프라인, 온/오프라인)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Period period; // 게시물 -> 프로젝트 or 스터디 기간

    @Column(nullable = false)
    private LocalDate deadline; // 게시물 마감일

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LinkType linkType;

    @Column(nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.RECRUITING; // 게시물에 대한 상태 (모집중, 마감)

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 게시물 생성날짜

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt; // 게시물 수정날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CultureFit cultureFit;

    // 정적 팩토리 메서드
    public static Post of(
            Long userId,
            String title,
            String content,
            RecruitType recruitType,
            Integer recruitMember,
            ProgressMethod progressMethod,
            Period period,
            LocalDate deadline,
            LinkType linkType,
            String link
            ) {

        Post post = new Post();
        post.userId = userId;
        post.title = title;
        post.content = content;
        post.recruitType = recruitType;
        post.recruitMember = recruitMember;
        post.progressMethod = progressMethod;
        post.period = period;
        post.deadline = deadline;
        post.status = Status.RECRUITING; // 기본값 설정
        post.linkType = linkType;
        post.link = link;
        post.cultureFit = CultureFit.NONE;

        return post;
    }

    public void updatePost(PostUpdateDto dto) {
        this.title = dto.title();
        this.content = dto.content();
        this.recruitType = dto.recruitType();
        this.recruitMember = dto.recruitMember();
        this.progressMethod = dto.progressMethod();
        this.period = dto.period();
        this.deadline = dto.deadline();
        this.linkType = dto.linkType();
        this.link = dto.link();
        //this.cultureFit = dto.cultureFit();
        // createdAt은 수정하지 않음 (JPA에서 @UpdateTimestamp로 updatedAt은 자동 업데이트)
    }

    public void updateStatus(Status newStatus) {
        this.status = newStatus;
    }

    public void updateCultureFit(CultureFit cultureFit){
        this.cultureFit = cultureFit;
    }
}
