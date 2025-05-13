package com.eum.post.controller;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.response.PortfolioResponse;
import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.service.PortfolioService;
import com.eum.review.model.dto.response.PortfolioReviewResponse;
import com.eum.review.service.PeerReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final PeerReviewService peerReviewService;
    private final MemberRepository memberRepository;

    @GetMapping("/my")
    public ResponseEntity<List<PortfolioResponse>> getMyPortfolios(
            @RequestParam(required = false) String recruitType,
            HttpServletRequest httpServletRequest
    ) {
        UUID publicId = (UUID) httpServletRequest.getAttribute("publicId");

        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<PortfolioResponse> portfolios;
        if (recruitType != null) {
            // Enum으로 변환
            RecruitType type = RecruitType.fromString(recruitType);
            if (type == null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE,
                        "유효하지 않은 모집 타입입니다. PROJECT 또는 STUDY를 입력해주세요.");
            }
            portfolios = portfolioService.getUserPortfoliosByType(member.getId(), type);
        } else {
            portfolios = portfolioService.getUserPortfolios(member.getId());
        }
        return ResponseEntity.ok(portfolios);
    }

    @GetMapping("/{portfolioId}/reviews")
    public ResponseEntity<List<PortfolioReviewResponse>> getPortfolioReviews (
            @PathVariable Long portfolioId,
            HttpServletRequest httpServletRequest
    ){
        UUID publicId = (UUID) httpServletRequest.getAttribute("publicId");

        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<PortfolioReviewResponse> reviews = peerReviewService.getUserReviewsForPost(
                member.getId(), portfolioId);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<List<PortfolioResponse>> getUserPortfolios(
            @PathVariable UUID publicId,
            @RequestParam(required = false) String recruitType
    ) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<PortfolioResponse> portfolios;
        if (recruitType != null) {
            RecruitType type = RecruitType.fromString(recruitType);
            if (type == null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE,
                        "유효하지 않은 모집 타입입니다. PROJECT 또는 STUDY를 입력해주세요.");
            }
            portfolios = portfolioService.getUserPortfoliosByType(member.getId(), type);
        } else {
            portfolios = portfolioService.getUserPortfolios(member.getId());
        }
        return ResponseEntity.ok(portfolios);
    }

    @GetMapping("/{publicId}/{portfolioId}/reviews")
    public ResponseEntity<List<PortfolioReviewResponse>> getUserPortfolioReviews(
            @PathVariable UUID publicId,
            @PathVariable Long portfolioId
    ) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 포트폴리오 소유자 검증
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId);
        if (!portfolio.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.PORTFOLIO_ACCESS_DENIED);
        }

        List<PortfolioReviewResponse> reviews = peerReviewService.getUserReviewsForPost(
                member.getId(), portfolioId);

        return ResponseEntity.ok(reviews);
    }
}
