package com.eum.review.service;

import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;

public interface PeerReviewService {
    PeerReviewResponse createReview(PeerReviewCreateRequest request, Long reviewerUserId);
}
