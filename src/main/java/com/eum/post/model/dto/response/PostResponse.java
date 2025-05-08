package com.eum.post.model.dto.response;

import com.eum.post.model.dto.PostDto;
import com.eum.post.model.entity.enumerated.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostResponse(
        Long id,
        //Long userId,
        UUID publicId,
        String nickname,
        String title,
        String content,
        RecruitType recruitType,
        Integer recruitMember,
        ProgressMethod progressMethod,
        Period period,
        LocalDate deadline,
        LinkType linkType,
        String link,
        CultureFit cultureFit, //컬처핏 추가
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TechStackResponse> techStacks,
        List<PositionResponse> positions
) {

    public static PostResponse from(PostDto dto) {
        List<TechStackResponse> techStackResponses = dto.techStacks().stream()
                .map(TechStackResponse::from)
                .toList();

        List<PositionResponse> positionResponses = dto.positions().stream()
                .map(PositionResponse::from)
                .toList();

        return new PostResponse(
                dto.id(),
                dto.publicId(),
                dto.nickname(),
                dto.title(),
                dto.content(),
                dto.recruitType(),
                dto.recruitMember(),
                dto.progressMethod(),
                dto.period(),
                dto.deadline(),
                dto.linkType(),
                dto.link(),
                dto.cultureFit(),
                dto.status(),
                dto.createdAt(),
                dto.updatedAt(),
                techStackResponses,
                positionResponses
        );
    }
}
