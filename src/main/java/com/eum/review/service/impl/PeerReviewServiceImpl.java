package com.eum.review.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.PortfolioRepository;
import com.eum.post.model.repository.PostMemberRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.MemberReviewCommentResponse;
import com.eum.review.model.dto.response.MemberReviewScoreResponse;
import com.eum.review.model.dto.response.PortfolioReviewResponse;
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
    private final MemberRepository memberRepository;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    @Override
    public PeerReviewResponse createReview(PeerReviewCreateRequest request, Long reviewerMemberId, Long revieweeMemberId) {
        // 1. 자기 자신에 대한 리뷰가 아닌지 확인
        if (reviewerMemberId.equals(revieweeMemberId)){
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
        if (!postMemberRepository.existsByPostIdAndMemberId(request.postId(), reviewerMemberId)){
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED, "리뷰어는 해당 팀의 멤버여야 합니다.");
        }

        // 5. 리뷰 대상이 팀의 멤버인지 확인
        if (!postMemberRepository.existsByPostIdAndMemberId(request.postId(), revieweeMemberId)) {
            throw new CustomException(ErrorCode.REVIEW_TARGET_NOT_TEAM_MEMBER);
        }

        PeerReview peerReview = request.toEntity(reviewerMemberId, revieweeMemberId,post);
        PeerReview savedReview = peerReviewRepository.save(peerReview);

        Member reviewer = memberRepository.findById(reviewerMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member reviewee = memberRepository.findById(revieweeMemberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return PeerReviewResponse.of(
                savedReview.getId(),
                reviewer.getPublicId(),
                reviewee.getPublicId(),
                post.getId(),
                post.getTitle(),
                savedReview.getCollaborationScore(),
                savedReview.getTechnicalScore(),
                savedReview.getWorkAgainScore(),
                savedReview.getAverageScore(),
                savedReview.getReviewComment(),
                savedReview.getReviewDate()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public MemberReviewScoreResponse calculateUserReviewScore(Long userId) {
        Double overallAvgScore = peerReviewRepository.calculateOverallAverageScore(userId);

        if (overallAvgScore == null) {overallAvgScore = 0.0;}

        List<PeerReview> reviews = peerReviewRepository.findAllByRevieweeMemberId(userId);
        int reviewCount = reviews.size();

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberReviewScoreResponse.from(member.getPublicId(), overallAvgScore, reviewCount);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberReviewCommentResponse> getUserReviewComments(Long userId) {
        List<PeerReview> reviews = peerReviewRepository.findAllByRevieweeMemberId(userId);
        return reviews.stream()
                .map(MemberReviewCommentResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioReviewResponse> getUserReviewsForPost(Long userId, Long portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

        if (!portfolio.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.PORTFOLIO_ACCESS_DENIED);
        }

        List<PeerReview> reviews = peerReviewRepository.findAllByPostIdAndRevieweeMemberId(
                portfolio.getPostId(), userId);

        return reviews.stream()
                .map(review -> {
                    Member reviewer = memberRepository.findById(review.getReviewerMemberId())
                            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

                    return PortfolioReviewResponse.of(
                            review,
                            reviewer.getPublicId(),
                            reviewer.getNickname()
                    );
                })
                .collect(Collectors.toList());
    }
}
