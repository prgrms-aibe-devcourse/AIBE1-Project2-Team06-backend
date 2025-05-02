package com.eum.project.sevice.impl;

import com.eum.project.model.dto.PortfolioDto;
import com.eum.project.model.entity.Portfolio;
import com.eum.project.model.entity.Project;
import com.eum.project.model.repository.PortfolioRepository;
import com.eum.project.model.repository.ProjectRepository;
import com.eum.project.sevice.PortfolioService;
import com.eum.review.model.repository.PeerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final ProjectRepository projectRepository;
    private final PortfolioRepository portfolioRepository;
    private final PeerReviewRepository peerReviewRepository;

    @Override
    public PortfolioDto createPortfolio(Long userId, Long projectId, String link) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        Double averageScore = peerReviewRepository.calculateOverallAverageScore(userId);
        if (averageScore == null) {
            averageScore = 0.0;
        }

        Portfolio portfolio = Portfolio.of(
                userId,
                projectId,
                project.getProjectTitle(),
                link,
                averageScore
        );

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioDto.from(savedPortfolio);
    }

    @Override
    public List<PortfolioDto> getUserPortfolios(Long userId) {
        return List.of();
    }
}
