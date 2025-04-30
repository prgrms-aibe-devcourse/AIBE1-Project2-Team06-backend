package com.eum.project.model.dto.request;

import com.eum.project.model.entity.ProjectEntity;

import java.time.LocalDate;
import java.util.List;

public record ProjectRequest(
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
        List<Long> techStackIds,
        List<Long> positionIds
) {
    // Entity로 변환하는 메서드
    public ProjectEntity toEntity() {
        return ProjectEntity.of(
                userId,
                projectTitle,
                projectContent,
                recruitType,
                recruitMember,
                progressMethod,
                period,
                deadline,
                linkType,
                link
        );
    }
}
