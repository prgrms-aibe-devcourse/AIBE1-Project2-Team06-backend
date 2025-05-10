package com.eum.review.model.dto.request;

import com.eum.post.model.entity.Post;
import com.eum.review.model.entity.PeerReview;

import java.util.UUID;

public record PeerReviewCreateRequest(
        Long postId,
        UUID revieweePublicId,
        Integer collaborationScore,
        Integer technicalScore,
        Integer workAgainScore,
        String reviewComment
) {
    public PeerReview toEntity(Long reviewerMemberId, Long revieweeMemberId,Post post){
        return PeerReview.of(
                reviewerMemberId,
                revieweeMemberId,
                post,
                collaborationScore,
                technicalScore,
                workAgainScore,
                reviewComment
        );
    }
}
