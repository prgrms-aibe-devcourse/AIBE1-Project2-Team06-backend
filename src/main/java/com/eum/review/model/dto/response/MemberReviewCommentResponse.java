package com.eum.review.model.dto.response;

import com.eum.review.model.entity.PeerReview;

import java.time.LocalDateTime;

public record MemberReviewCommentResponse(
        String reviewComment,
        LocalDateTime reviewDate,
        Long postId,
        String postTitle
) {
    public static MemberReviewCommentResponse from(PeerReview peerReview){
        return new MemberReviewCommentResponse(
                peerReview.getReviewComment(),
                peerReview.getReviewDate(),
                peerReview.getPost().getId(),
                peerReview.getPost().getTitle()
        );
    }
}
