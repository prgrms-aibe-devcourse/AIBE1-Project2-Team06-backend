package com.eum.project.sevice.impl;

import com.eum.project.model.dto.PortfolioDto;
import com.eum.project.sevice.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    @Override
    public PortfolioDto createPortfolio(Long userId, Long projectId, String link) {
        return null;
    }

    @Override
    public List<PortfolioDto> getUserPortfolios(Long userId) {
        return List.of();
    }
}
