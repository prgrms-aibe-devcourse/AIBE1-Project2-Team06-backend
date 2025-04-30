package com.eum.post.model.dto.response;

import com.eum.post.model.dto.PostDto;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
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
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TechStackResponse> techStacks,
        List<PositionResponse> positions
) {

    public record TechStackResponse(Long id, String name) {}
    public record PositionResponse(Long id, String name) {}

    public static PostResponse from(PostDto dto) {
        List<TechStackResponse> techStackResponses = dto.techStacks().stream()
                .map(ts -> new TechStackResponse(ts.id(), ts.name()))
                .toList();

        List<PositionResponse> positionResponses = dto.positions().stream()
                .map(p -> new PositionResponse(p.id(), p.name()))
                .toList();

        return new PostResponse(
                dto.id(),
                dto.userId(),
                dto.title(),
                dto.content(),
                dto.recruitType(),
                dto.recruitMember(),
                dto.progressMethod(),
                dto.period(),
                dto.deadline(),
                dto.linkType(),
                dto.link(),
                dto.status(),
                dto.createdAt(),
                dto.updatedAt(),
                techStackResponses,
                positionResponses
        );
    }
}
