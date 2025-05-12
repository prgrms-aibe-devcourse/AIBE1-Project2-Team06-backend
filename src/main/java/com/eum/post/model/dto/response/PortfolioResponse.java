package com.eum.post.model.dto.response;

import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.enumerated.RecruitType;

import java.time.LocalDateTime;

public record PortfolioResponse(
        Long id,
        Long postId,
        String postTitle,
        String postLink,
        Double averageScore,
        RecruitType recruitType,
        LocalDateTime createAt
) {
    public static PortfolioResponse from(Portfolio portfolio) {
        return new PortfolioResponse(
                portfolio.getId(),
                portfolio.getPostId(),
                portfolio.getPostTitle(),
                portfolio.getPostLink(),
                portfolio.getAverageScore(),
                portfolio.getRecruitType(),
                portfolio.getCreatedAt()
        );
    }
}
