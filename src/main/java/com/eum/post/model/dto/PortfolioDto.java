package com.eum.post.model.dto;

import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.enumerated.RecruitType;

import java.time.LocalDateTime;

public record PortfolioDto(
        Long id,
        Long useId,
        Long postId,
        String postTitle,
        String postLink,
        Double averageScore,
        RecruitType recruitType,
        LocalDateTime createAt
) {
    public static PortfolioDto from(Portfolio portfolio) {
        return new PortfolioDto(
                portfolio.getId(),
                portfolio.getUserId(),
                portfolio.getPostId(),
                portfolio.getPostTitle(),
                portfolio.getPostLink(),
                portfolio.getAverageScore(),
                portfolio.getRecruitType(),
                portfolio.getCreatedAt()
        );
    }
}
