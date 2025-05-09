package com.eum.review.model.dto.response;

import com.eum.post.model.entity.Post;
import com.eum.review.model.entity.PeerReview;

import java.time.LocalDateTime;

public record PeerReviewResponse(
        Integer id,
        Long reviewerMemberId,
        Long revieweeMemberId,
        Post post,
        Integer collaborationScore,
        Integer technicalScore,
        Integer workAgainScore,
        Double averageScore,
        String reviewComment,
        LocalDateTime reviewDate
) {
    public static PeerReviewResponse from(PeerReview peerReview){
        return new PeerReviewResponse(
                peerReview.getId(),
                peerReview.getReviewerMemberId(),
                peerReview.getRevieweeMemberId(),
                peerReview.getPost(),
                peerReview.getCollaborationScore(),
                peerReview.getTechnicalScore(),
                peerReview.getWorkAgainScore(),
                peerReview.getAverageScore(),
                peerReview.getReviewComment(),
                peerReview.getReviewDate()
        );
    }
}
