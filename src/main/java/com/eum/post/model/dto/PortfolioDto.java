package com.eum.post.model.dto;

import com.eum.post.model.entity.Portfolio;

public record PortfolioDto(
        Long id,
        Long useId,
        Long postId,
        String postTitle,
        String postLink,
        Double averageScore
) {
    public static PortfolioDto from(Portfolio portfolio) {
        return new PortfolioDto(
                portfolio.getId(),
                portfolio.getUserId(),
                portfolio.getPostId(),
                portfolio.getPostTitle(),
                portfolio.getPostLink(),
                portfolio.getAverageScore()
        );
    }
}
