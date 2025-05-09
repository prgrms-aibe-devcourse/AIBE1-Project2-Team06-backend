package com.eum.review.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.PostRepository;
import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.UserReviewCommentResponse;
import com.eum.review.model.dto.response.UserReviewScoreResponse;
import com.eum.review.model.entity.PeerReview;
import com.eum.review.model.repository.PeerReviewRepository;
import com.eum.review.service.PeerReviewService;
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
        if (reviewerUserId.equals(request.revieweeMemberId())){
            throw new CustomException(ErrorCode.SELF_REVIEW_NOT_ALLOWED);
        }

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getStatus().equals(Status.COMPLETED)) {
            throw new CustomException(ErrorCode.POST_NOT_COMPLETE);
        }

        // TODO: 리뷰 작성자와 대상자가 프로젝트의 팀원인지 검증하는 로직 추가 필요
        // 팀원 관계 테이블 개발 완료 후 코드 추가 예정

        PeerReview peerReview = request.toEntity(reviewerUserId, post);
        PeerReview savedReview = peerReviewRepository.save(peerReview);

        return PeerReviewResponse.from(savedReview);
    }

    @Transactional(readOnly = true)
    @Override
    public UserReviewScoreResponse calculateUserReviewScore(Long userId) {
        Double overallAvgScore = peerReviewRepository.calculateOverallAverageScore(userId);

        if (overallAvgScore == null) {overallAvgScore = 0.0;}

        List<PeerReview> reviews = peerReviewRepository.findAllByRevieweeMemberId(userId);
        int reviewCount = reviews.size();

        return UserReviewScoreResponse.from(userId, overallAvgScore, reviewCount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserReviewCommentResponse> getUserReviewComments(Long userId) {
        List<PeerReview> reviews = peerReviewRepository.findAllByRevieweeMemberId(userId);
        return reviews.stream()
                .map(UserReviewCommentResponse::from)
                .collect(Collectors.toList());
    }
}
