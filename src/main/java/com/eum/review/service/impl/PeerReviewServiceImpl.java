package com.eum.review.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.PostMemberRepository;
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
    private final PostMemberRepository postMemberRepository;

    @Transactional
    @Override
    public PeerReviewResponse createReview(PeerReviewCreateRequest request, Long reviewerUserId) {
        // 1. 자기 자신에 대한 리뷰가 아닌지 확인
        if (reviewerUserId.equals(request.revieweeMemberId())){
            throw new CustomException(ErrorCode.SELF_REVIEW_NOT_ALLOWED);
        }

        // 2. 게시글이 있는지 확인
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 3. 게시글이 완료상태인지 확인
        if (!post.getStatus().equals(Status.COMPLETED)) {
            throw new CustomException(ErrorCode.POST_NOT_COMPLETE);
        }

        // 4. 리뷰어가 팀의 멤버인지 확인
        if (!postMemberRepository.existsByPostIdAndMemberId(request.postId(), reviewerUserId)){
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED, "리뷰어는 해당 팀의 멤버여야 합니다.");
        }

        // 5. 리뷰 대상이 팀의 멤버인지 확인
        if (!postMemberRepository.existsByPostIdAndMemberId(request.postId(), request.revieweeMemberId())) {
            throw new CustomException(ErrorCode.REVIEW_TARGET_NOT_TEAM_MEMBER);
        }

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
