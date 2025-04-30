package com.eum.project.model.dto;

import com.eum.project.model.entity.Portfolio;

public record PortfolioDto(
        Long id,
        Long useId,
        Long projectId,
        String projectTitle,
        String projectLink,
        Double averageScore
) {
    public static PortfolioDto from(Portfolio portfolio) {
        return new PortfolioDto(
                portfolio.getId(),
                portfolio.getUserId(),
                portfolio.getProjectId(),
                portfolio.getProjectTitle(),
                portfolio.getProjectLink(),
                portfolio.getAverageScore()
        );
    }
}
