package com.eum.review.service;

import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.MemberReviewCommentResponse;
import com.eum.review.model.dto.response.MemberReviewScoreResponse;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.PortfolioReviewResponse;

import java.util.List;

public interface PeerReviewService {
    PeerReviewResponse createReview(PeerReviewCreateRequest request, Long reviewerUserId, Long revieweeUserId);
    MemberReviewScoreResponse calculateUserReviewScore(Long userId);
    List<MemberReviewCommentResponse> getUserReviewComments(Long userId);
    List<PortfolioReviewResponse> getUserReviewsForPost(Long userId, Long portfolioId);
}
