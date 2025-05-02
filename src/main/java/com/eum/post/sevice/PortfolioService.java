package com.eum.post.sevice;

import com.eum.post.model.dto.PortfolioDto;

import java.util.List;

public interface PortfolioService {
    PortfolioDto createPortfolio(Long userId, Long projectId, String link);
    List<PortfolioDto> getUserPortfolios(Long userId);
}
