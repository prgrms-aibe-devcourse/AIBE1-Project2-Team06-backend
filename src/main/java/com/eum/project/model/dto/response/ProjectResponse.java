package com.eum.project.model.dto.response;

import com.eum.project.model.entity.ProjectEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(
        Long id,
        Long userId,
        String projectTitle,
        String projectContent,
        ProjectEntity.RecruitType recruitType,
        Integer recruitMember,
        ProjectEntity.ProgressMethod progressMethod,
        ProjectEntity.Period period,
        LocalDate deadline,
        String linkType,
        String link,
        ProjectEntity.Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TechStackResponse> techStacks
) {
    public record TechStackResponse(
            Long id,
            String name
    ) {}

    // Entity와 기술 스택 정보를 함께 Response로 변환하는 정적 메서드
    public static ProjectResponse from(
            ProjectEntity entity,
            List<TechStackResponse> techStacks) {

        return new ProjectResponse(
                entity.getId(),
                entity.getUserId(),
                entity.getProjectTitle(),
                entity.getProjectContent(),
                entity.getRecruitType(),
                entity.getRecruitMember(),
                entity.getProgressMethod(),
                entity.getPeriod(),
                entity.getDeadline(),
                entity.getLinkType(),
                entity.getLink(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                techStacks
        );
    }
}
