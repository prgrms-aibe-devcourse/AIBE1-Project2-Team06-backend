package com.eum.review.model.dto.response;

import java.util.UUID;

public record MemberReviewScoreResponse(
        UUID memberPublicId,
        Double overallAverageScore,
        Integer reviewCount
) {
    public static MemberReviewScoreResponse from(
            UUID memberPublicId,
            Double overallAverageScore,
            Integer reviewCount
    ) {
        return new MemberReviewScoreResponse(memberPublicId, overallAverageScore, reviewCount);
    }
}
