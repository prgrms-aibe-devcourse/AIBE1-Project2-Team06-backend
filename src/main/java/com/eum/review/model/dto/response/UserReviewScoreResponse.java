package com.eum.review.model.dto.response;

import java.util.UUID;

public record UserReviewScoreResponse(
        UUID memberPublicId,
        Double overallAverageScore,
        Integer reviewCount
) {
    public static UserReviewScoreResponse from(
            UUID memberPublicId,
            Double overallAverageScore,
            Integer reviewCount
    ) {
        return new UserReviewScoreResponse(memberPublicId, overallAverageScore, reviewCount);
    }
}
