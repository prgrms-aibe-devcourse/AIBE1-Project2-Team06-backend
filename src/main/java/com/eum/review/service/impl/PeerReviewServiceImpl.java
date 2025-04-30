package com.eum.review.service.impl;

import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.entity.PeerReview;
import com.eum.review.model.repository.PeerReviewRepository;
import com.eum.review.service.PeerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PeerReviewServiceImpl implements PeerReviewService {
    private final PeerReviewRepository peerReviewRepository;

    @Override
    public PeerReviewResponse createReview(PeerReviewCreateRequest request, Long reviewerUserId) {
        if (reviewerUserId.equals(request.revieweeUserId())){
            throw new IllegalArgumentException("자기 자신에 대한 리뷰는 작성할 수 없습니다.");
        }

        PeerReview peerReview = request.toEntity(reviewerUserId);
        PeerReview savedReview = peerReviewRepository.save(peerReview);

        return PeerReviewResponse.from(savedReview);
    }
}
