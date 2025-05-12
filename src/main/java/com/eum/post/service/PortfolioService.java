package com.eum.post.service;

import com.eum.post.model.dto.PortfolioDto;
import com.eum.post.model.dto.response.PortfolioResponse;
import com.eum.post.model.entity.enumerated.RecruitType;

import java.util.List;

public interface PortfolioService {
    PortfolioDto createPortfolio(Long userId, Long postId, String link);
    List<PortfolioResponse> getUserPortfolios(Long userId);
    List<PortfolioResponse> getUserPortfoliosByType(Long userId, RecruitType recruitType);
}
