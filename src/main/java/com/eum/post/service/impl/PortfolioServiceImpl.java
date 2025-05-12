package com.eum.post.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.member.model.entity.Member;
import com.eum.post.model.dto.PortfolioDto;
import com.eum.post.model.dto.response.PortfolioResponse;
import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.model.repository.PortfolioRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.post.service.PortfolioService;
import com.eum.review.model.repository.PeerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PostRepository postRepository;
    private final PortfolioRepository portfolioRepository;
    private final PeerReviewRepository peerReviewRepository;

    @Override
    public PortfolioDto createPortfolio(Member member, Long postId, String link) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Double averageScore = peerReviewRepository.calculateOverallAverageScore(member.getId());
        if (averageScore == null) {
            averageScore = 0.0;
        }

        Portfolio portfolio = Portfolio.of(
                member,
                postId,
                post.getTitle(),
                link,
                averageScore,
                post.getRecruitType()
        );

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        return PortfolioDto.from(savedPortfolio);
    }

    @Override
    public List<PortfolioResponse> getUserPortfolios(Long userId) {
        List<Portfolio> portfolios = portfolioRepository.findAllByMemberId(userId);

        return portfolios.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioResponse> getUserPortfoliosByType(Long userId, RecruitType recruitType) {
        List<Portfolio> portfolios = portfolioRepository.findAllByMemberIdAndRecruitType(userId, recruitType);

        return portfolios.stream()
                .map(PortfolioResponse::from)
                .collect(Collectors.toList());
    }
}
