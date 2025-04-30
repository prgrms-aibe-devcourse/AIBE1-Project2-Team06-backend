package com.eum.review.service;

import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.UserReviewCommentResponse;
import com.eum.review.model.dto.response.UserReviewScoreResponse;

import java.util.List;

public interface PeerReviewService {
    PeerReviewResponse createReview(PeerReviewCreateRequest request, Long reviewerUserId);
    UserReviewScoreResponse calculateUserReviewScore(Long userId);
    List<UserReviewCommentResponse> getUserReviewComments(Long userId);
}
