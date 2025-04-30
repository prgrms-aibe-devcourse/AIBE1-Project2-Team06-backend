package com.eum.review.model.dto.request;

import com.eum.review.model.entity.PeerReview;

public record PeerReviewCreateRequest(
        Long projectId,
        Long revieweeUserId,
        Integer collaborationScore,
        Integer technicalScore,
        Integer workAgainScore,
        String reviewComment
) {
    public PeerReview toEntity(Long reviewerUserId){
        return PeerReview.of(
                reviewerUserId,
                revieweeUserId,
                projectId,
                collaborationScore,
                technicalScore,
                workAgainScore,
                reviewComment
        );
    }
}
