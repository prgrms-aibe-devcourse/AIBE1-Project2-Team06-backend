package com.eum.review.model.dto.response;

public record UserReviewSummaryResponse(
        Long userId,
        Double overallAverageScore,
        Integer reviewCount
) {
    public static UserReviewSummaryResponse from(
            Long userId,
            Double overallAverageScore,
            Integer reviewCount
    ) {
        return new UserReviewSummaryResponse(userId, overallAverageScore, reviewCount);
    }
}
