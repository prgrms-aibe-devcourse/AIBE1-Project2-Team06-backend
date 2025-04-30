package com.eum.project.model.dto.response;

import com.eum.project.model.entity.Project;
import com.eum.project.model.entity.enumerated.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponse(
        Long id,
        Long userId,
        String projectTitle,
        String projectContent,
        RecruitType recruitType,
        Integer recruitMember,
        ProgressMethod progressMethod,
        Period period,
        LocalDate deadline,
        LinkType linkType,
        String link,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TechStackResponse> techStacks,
        List<PositionResponse> positions
) {
    public record TechStackResponse(
            Long id,
            String name
    ) {}

    public record PositionResponse(
            Long id,
            String name
    ){}

    // Entity와 기술 스택 정보를 함께 Response로 변환하는 정적 메서드
    public static ProjectResponse from(
            Project project,
            List<TechStackResponse> techStacks,
            List<PositionResponse> positions) {

        return new ProjectResponse(
                project.getId(),
                project.getUserId(),
                project.getTitle(),
                project.getContent(),
                project.getRecruitType(),
                project.getRecruitMember(),
                project.getProgressMethod(),
                project.getPeriod(),
                project.getDeadline(),
                project.getLinkType(),
                project.getLink(),
                project.getStatus(),
                project.getCreatedAt(),
                project.getUpdatedAt(),
                techStacks,
                positions
        );
    }
}
