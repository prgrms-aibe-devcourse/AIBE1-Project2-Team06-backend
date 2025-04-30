package com.eum.review.model.dto.response;

public record UserReviewScoreResponse(
        Long userId,
        Double overallAverageScore,
        Integer reviewCount
) {
    public static UserReviewScoreResponse from(
            Long userId,
            Double overallAverageScore,
            Integer reviewCount
    ) {
        return new UserReviewScoreResponse(userId, overallAverageScore, reviewCount);
    }
}
