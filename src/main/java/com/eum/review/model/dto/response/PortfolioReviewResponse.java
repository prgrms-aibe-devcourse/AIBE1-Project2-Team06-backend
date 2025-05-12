package com.eum.review.model.dto.response;

import com.eum.review.model.entity.PeerReview;

import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioReviewResponse(
        Integer id,
        UUID reviewerPublicId,
        String reviewerNickname,  // 리뷰어 닉네임 추가
        Integer collaborationScore,
        Integer technicalScore,
        Integer workAgainScore,
        Double averageScore,
        String reviewComment,
        LocalDateTime reviewDate
) {
    public static PortfolioReviewResponse of(
            PeerReview review,
            UUID reviewerPublicId,
            String reviewerNickname
    ) {
        return new PortfolioReviewResponse(
                review.getId(),
                reviewerPublicId,
                reviewerNickname,
                review.getCollaborationScore(),
                review.getTechnicalScore(),
                review.getWorkAgainScore(),
                review.getAverageScore(),
                review.getReviewComment(),
                review.getReviewDate()
        );
    }
}
