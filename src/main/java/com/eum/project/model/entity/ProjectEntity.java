package com.eum.project.model.entity;

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
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시물 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 모집자 ID

    @Column(name = "project_title", nullable = false , length = 255)
    private String projectTitle; // 게시물 제목

    @Column(name = "project_content", nullable = false, columnDefinition = "TEXT")
    private String projectContent; // 게시물 내용

    @Enumerated(EnumType.STRING)
    @Column(name = "recruit_type", nullable = false)
    private RecruitType recruitType; // 게시물이 스터디, 프로젝트

    @Column(name = "recruit_member", nullable = false)
    private Integer recruitMember; // 모집 인원

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_method", nullable = false)
    private ProgressMethod progressMethod; // 진행 방식 (온라인, 오프라인, 온/오프라인)

    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false)
    private Period period; // 게시물 -> 프로젝트 or 스터디 기간

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline; // 게시물 마감일

    @Column(name = "link_type", nullable = false)
    private String linkType;

    @Column(name = "link", nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.RECRUITING; // 게시물에 대한 상태 (모집중, 마감)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt; // 게시물 생성날짜

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 게시물 수정날짜

    // Enum 정의
    public enum RecruitType {
        STUDY,
        PROJECT;
    }

    public enum ProgressMethod {
        ONLINE,
        OFFLINE,
        ALL;
    }

    public enum Period {
        TDB, MONTH_1, MONTH_2, MONTH_3,
        MONTH_4, MONTH_5, MONTH_6, LONG_TERM;
    }

    public enum Status {
        RECRUITING,
        CLOSED,
        ONGOING,
        COMPLETED;
    }

    // 정적 팩토리 메서드
    public static ProjectEntity of(
            Long userId,
            String projectTitle,
            String projectContent,
            RecruitType recruitType,
            Integer recruitMember,
            ProgressMethod progressMethod,
            Period period,
            LocalDate deadline,
            String linkType,
            String link
            ) {

        ProjectEntity project = new ProjectEntity();
        project.userId = userId;
        project.projectTitle = projectTitle;
        project.projectContent = projectContent;
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
