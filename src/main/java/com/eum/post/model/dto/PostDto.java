package com.eum.post.model.dto;

import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostDto(
        Long id,
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
                post.getMemberPublicId(),
                post.getMemberNickname(),
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
