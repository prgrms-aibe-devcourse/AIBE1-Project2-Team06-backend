package com.eum.review.model.dto.request;

import com.eum.post.model.entity.Post;
import com.eum.review.model.entity.PeerReview;

public record PeerReviewCreateRequest(
        Long postId,
        Long revieweeUserId,
        Integer collaborationScore,
        Integer technicalScore,
        Integer workAgainScore,
        String reviewComment
) {
    public PeerReview toEntity(Long reviewerUserId, Post post){
        return PeerReview.of(
                reviewerUserId,
                revieweeUserId,
                post,
                collaborationScore,
                technicalScore,
                workAgainScore,
                reviewComment
        );
    }
}
