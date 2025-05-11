package com.eum.review.model.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PeerReviewResponse(
        Integer id,
        UUID reviewerPublicId,
        UUID revieweePublicId,
        Long postId,
        String postTitle,
        Integer collaborationScore,
        Integer technicalScore,
        Integer workAgainScore,
        Double averageScore,
        String reviewComment,
        LocalDateTime reviewDate
) {
    public static PeerReviewResponse of(
            Integer id,
            UUID reviewerPublicId,
            UUID revieweePublicId,
            Long postId,
            String postTitle,
            Integer collaborationScore,
            Integer technicalScore,
            Integer workAgainScore,
            Double averageScore,
            String reviewComment,
            LocalDateTime reviewDate
    ){
        return new PeerReviewResponse(
                id,
                reviewerPublicId,
                revieweePublicId,
                postId,
                postTitle,
                collaborationScore,
                technicalScore,
                workAgainScore,
                averageScore,
                reviewComment,
                reviewDate
        );
    }
}
