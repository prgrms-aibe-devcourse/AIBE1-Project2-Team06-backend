package com.eum.post.service;

import com.eum.post.model.dto.PortfolioDto;

import java.util.List;

public interface PortfolioService {
    PortfolioDto createPortfolio(Long userId, Long postId, String link);
    List<PortfolioDto> getUserPortfolios(Long userId);
}
