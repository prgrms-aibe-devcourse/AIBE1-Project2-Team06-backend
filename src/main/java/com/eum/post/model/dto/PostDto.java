package com.eum.post.model.dto;

import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PostDto(
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
        CultureFit cultureFit,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<TechStackDto> techStacks,
        List<PositionDto> positions
) {

    public static PostDto from(Post post, List<TechStackDto> techStacks, List<PositionDto> positions) {
        return new PostDto(
                post.getId(),
                post.getUserId(),
                post.getTitle(),
                post.getContent(),
                post.getRecruitType(),
                post.getRecruitMember(),
                post.getProgressMethod(),
                post.getPeriod(),
                post.getDeadline(),
                post.getLinkType(),
                post.getLink(),
                post.getCultureFit(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                techStacks,
                positions
        );
    }
}
