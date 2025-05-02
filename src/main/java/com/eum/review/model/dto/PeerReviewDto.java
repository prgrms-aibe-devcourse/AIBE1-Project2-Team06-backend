package com.eum.review.model.dto;

import com.eum.post.model.entity.Post;
import com.eum.review.model.entity.PeerReview;

public record PeerReviewDto(
        Integer id,
        Long reviewerUserId,
        Long revieweeUserId,
        Post post,
        Integer collaborationScore,
        Integer technicalScore,
        Integer workAgainScore,
        Double averageScore,
        String reviewComment
) {
    public static PeerReviewDto from(PeerReview peerReview) {
        return new PeerReviewDto(
                peerReview.getId(),
                peerReview.getReviewerUserId(),
                peerReview.getRevieweeUserId(),
                peerReview.getPost(),
                peerReview.getCollaborationScore(),
                peerReview.getTechnicalScore(),
                peerReview.getWorkAgainScore(),
                peerReview.getAverageScore(),
                peerReview.getReviewComment()
        );
    }
}
