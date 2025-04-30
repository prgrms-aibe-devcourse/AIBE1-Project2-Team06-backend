package com.eum.project.model.dto.request;

import com.eum.project.model.entity.Project;
import com.eum.project.model.entity.enumerated.LinkType;
import com.eum.project.model.entity.enumerated.Period;
import com.eum.project.model.entity.enumerated.ProgressMethod;
import com.eum.project.model.entity.enumerated.RecruitType;

import java.time.LocalDate;
import java.util.List;

public record ProjectRequest(
        Long userId,
        String title,
        String content,
        RecruitType recruitType,
        Integer recruitMember,
        ProgressMethod progressMethod,
        Period period,
        LocalDate deadline,
        LinkType linkType,
        String link,
        List<Long> techStackIds,
        List<Long> positionIds
) {
    // Entity로 변환하는 메서드
    public Project toEntity() {
        return Project.of(
                userId,
                title,
                content,
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
