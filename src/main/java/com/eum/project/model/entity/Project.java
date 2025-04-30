package com.eum.project.model.entity;

import com.eum.project.model.entity.enumerated.*;
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
@Table(name = "Project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

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


    // 정적 팩토리 메서드
    public static Project of(
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

        Project project = new Project();
        project.userId = userId;
        project.title = title;
        project.content = content;
        project.recruitType = recruitType;
        project.recruitMember = recruitMember;
        project.progressMethod = progressMethod;
        project.period = period;
        project.deadline = deadline;
        project.status = Status.RECRUITING; // 기본값 설정
        project.linkType = linkType;
        project.link = link;

        return project;
    }
}
