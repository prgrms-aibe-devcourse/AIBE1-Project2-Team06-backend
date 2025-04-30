package com.eum.review.model.dto.response;

import com.eum.review.model.entity.PeerReview;

import java.time.LocalDateTime;

public record UserReviewCommentResponse(
        String reviewComment,
        LocalDateTime reviewDate,
        Long projectId
) {
    public static UserReviewCommentResponse from(PeerReview peerReview){
        return new UserReviewCommentResponse(
                peerReview.getReviewComment(),
                peerReview.getReviewDate(),
                peerReview.getProjectId()
        );
    }
}
