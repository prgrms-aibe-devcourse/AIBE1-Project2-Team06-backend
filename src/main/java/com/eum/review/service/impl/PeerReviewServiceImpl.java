package com.eum.review.service.impl;

import com.eum.post.model.entity.Post;
import com.eum.post.model.repository.PostRepository;
import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.UserReviewCommentResponse;
import com.eum.review.model.dto.response.UserReviewScoreResponse;
import com.eum.review.model.entity.PeerReview;
import com.eum.review.model.repository.PeerReviewRepository;
import com.eum.review.service.PeerReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PeerReviewServiceImpl implements PeerReviewService {
    private final PeerReviewRepository peerReviewRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public PeerReviewResponse createReview(PeerReviewCreateRequest request, Long reviewerUserId) {
        if (reviewerUserId.equals(request.revieweeUserId())){
            throw new IllegalArgumentException("자기 자신에 대한 리뷰는 작성할 수 없습니다.");
        }

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("Post가 존재하지 않습니다. ID : " + request.postId()));

        PeerReview peerReview = request.toEntity(reviewerUserId, post);
        PeerReview savedReview = peerReviewRepository.save(peerReview);

        return PeerReviewResponse.from(savedReview);
    }

    @Transactional(readOnly = true)
    @Override
    public UserReviewScoreResponse calculateUserReviewScore(Long userId) {
        Double overallAvgScore = peerReviewRepository.calculateOverallAverageScore(userId);

        if (overallAvgScore == null) {overallAvgScore = 0.0;}

        List<PeerReview> reviews = peerReviewRepository.findAllByRevieweeUserId(userId);
        int reviewCount = reviews.size();

        return UserReviewScoreResponse.from(userId, overallAvgScore, reviewCount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserReviewCommentResponse> getUserReviewComments(Long userId) {
        List<PeerReview> reviews = peerReviewRepository.findAllByRevieweeUserId(userId);
        return reviews.stream()
                .map(UserReviewCommentResponse::from)
                .collect(Collectors.toList());
    }
}
