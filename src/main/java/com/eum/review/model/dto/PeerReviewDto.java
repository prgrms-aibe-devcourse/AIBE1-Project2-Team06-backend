package com.eum.review.model.dto;

import com.eum.post.model.entity.Post;
import com.eum.review.model.entity.PeerReview;

public record PeerReviewDto(
        Integer id,
        Long reviewerMemberId,
        Long revieweeMemberId,
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
                peerReview.getReviewerMemberId(),
                peerReview.getRevieweeMemberId(),
                peerReview.getPost(),
                peerReview.getCollaborationScore(),
                peerReview.getTechnicalScore(),
                peerReview.getWorkAgainScore(),
                peerReview.getAverageScore(),
                peerReview.getReviewComment()
        );
    }
}
