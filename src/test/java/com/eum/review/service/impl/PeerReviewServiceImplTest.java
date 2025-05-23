package com.eum.review.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.PostMemberRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.MemberReviewCommentResponse;
import com.eum.review.model.dto.response.MemberReviewScoreResponse;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.entity.PeerReview;
import com.eum.review.model.repository.PeerReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PeerReviewServiceImplTest {

    @InjectMocks
    private PeerReviewServiceImpl peerReviewService;

    @Mock
    private PeerReviewRepository peerReviewRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMemberRepository postMemberRepository;

    @Mock
    private MemberRepository memberRepository;

    private Post testPost;
    private PeerReview testPeerReview;
    private PeerReviewCreateRequest testRequest;
    private Long reviewerMemberId;
    private Long revieweeMemberId;
    private UUID reviewerPublicId;
    private UUID revieweePublicId;
    private Long postId;
    private Member reviewerMember;
    private Member revieweeMember;

    @BeforeEach
    void setUp() {
        reviewerMemberId = 1L;
        revieweeMemberId = 2L;
        reviewerPublicId = UUID.randomUUID();
        revieweePublicId = UUID.randomUUID();
        postId = 1L;

        reviewerMember = mock(Member.class);
        lenient().when(reviewerMember.getId()).thenReturn(reviewerMemberId);
        lenient().when(reviewerMember.getPublicId()).thenReturn(reviewerPublicId);

        revieweeMember = mock(Member.class);
        lenient().when(revieweeMember.getId()).thenReturn(revieweeMemberId);
        lenient().when(revieweeMember.getPublicId()).thenReturn(revieweePublicId);

        // memberRepository 모킹
        lenient().when(memberRepository.findById(reviewerMemberId)).thenReturn(Optional.of(reviewerMember));
        lenient().when(memberRepository.findById(revieweeMemberId)).thenReturn(Optional.of(revieweeMember));

        testPost = mock(Post.class);
        lenient().when(testPost.getId()).thenReturn(postId);
        lenient().when(testPost.getTitle()).thenReturn("테스트 프로젝트");
        lenient().when(testPost.getStatus()).thenReturn(Status.COMPLETED);

        testRequest = new PeerReviewCreateRequest(
                postId,
                revieweePublicId,
                5,
                4,
                5,
                "좋은 팀원이였습니다."
        );

        testPeerReview = mock(PeerReview.class);
        lenient().when(testPeerReview.getId()).thenReturn(1);
        lenient().when(testPeerReview.getReviewerMemberId()).thenReturn(reviewerMemberId);
        lenient().when(testPeerReview.getRevieweeMemberId()).thenReturn(revieweeMemberId);
        lenient().when(testPeerReview.getPost()).thenReturn(testPost);
        lenient().when(testPeerReview.getCollaborationScore()).thenReturn(5);
        lenient().when(testPeerReview.getTechnicalScore()).thenReturn(4);
        lenient().when(testPeerReview.getWorkAgainScore()).thenReturn(5);
        lenient().when(testPeerReview.getAverageScore()).thenReturn(4.7);
        lenient().when(testPeerReview.getReviewComment()).thenReturn("좋은 팀원이었습니다.");
        lenient().when(testPeerReview.getReviewDate()).thenReturn(LocalDateTime.now());
    }

    @Test
    @DisplayName("리뷰 생성 - 성공")
    void createReviewSuccess() {
        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));

        given(postMemberRepository.existsByPostIdAndMemberId(postId, revieweeMemberId)).willReturn(true);
        given(postMemberRepository.existsByPostIdAndMemberId(postId, reviewerMemberId)).willReturn(true);
        given(peerReviewRepository.save(any(PeerReview.class))).willReturn(testPeerReview);

        PeerReviewResponse response = peerReviewService.createReview(testRequest, reviewerMemberId, revieweeMemberId);

        assertNotNull(response);
        assertEquals(testPeerReview.getId(), response.id());

        assertEquals(reviewerPublicId, response.reviewerPublicId());
        assertEquals(revieweePublicId, response.revieweePublicId());
        assertEquals(testPeerReview.getAverageScore(), response.averageScore());
        assertEquals("좋은 팀원이었습니다.", response.reviewComment());
        assertEquals(postId, response.postId());
        assertEquals("테스트 프로젝트", response.postTitle());

        verify(postRepository, times(1)).findById(postId);
        verify(postMemberRepository, times(1)).existsByPostIdAndMemberId(postId, reviewerMemberId);
        verify(postMemberRepository, times(1)).existsByPostIdAndMemberId(postId, revieweeMemberId);
        verify(peerReviewRepository, times(1)).save(any(PeerReview.class));
        verify(memberRepository, times(1)).findById(reviewerMemberId);
        verify(memberRepository, times(1)).findById(revieweeMemberId);
    }

    @Test
    @DisplayName("리뷰 생성 - 자기 자신 리뷰 실패")
    void createReviewSelfReviewFail() {
        Long sameUserId = 1L;
        UUID samePublicId = UUID.randomUUID();

        PeerReviewCreateRequest selfReviewRequest = new PeerReviewCreateRequest(
                postId,
                samePublicId,
                5, 4, 5,
                "자기 자신 리뷰"
        );

        CustomException exception = assertThrows(CustomException.class, () ->
                peerReviewService.createReview(selfReviewRequest, sameUserId, sameUserId));

        assertEquals(ErrorCode.SELF_REVIEW_NOT_ALLOWED, exception.getErrorCode());
        verify(postRepository, never()).findById(anyLong());
        verify(peerReviewRepository, never()).save(any(PeerReview.class));
    }

    @Test
    @DisplayName("리뷰 생성 - 게시글 없음 실패")
    void createReview_PostNotFound_Fail() {
        // given
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                peerReviewService.createReview(testRequest, reviewerMemberId, revieweeMemberId)
        );

        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
        verify(postRepository, times(1)).findById(postId);
        verify(peerReviewRepository, never()).save(any(PeerReview.class));
    }

    @Test
    @DisplayName("사용자 리뷰 점수 계산 - 성공")
    void calculateUserReviewScoreSuccess() {
        Double avgScore = 4.7;
        List<PeerReview> reviews = Arrays.asList(testPeerReview);

        given(peerReviewRepository.calculateOverallAverageScore(revieweeMemberId)).willReturn(avgScore);
        given(peerReviewRepository.findAllByRevieweeMemberId(revieweeMemberId)).willReturn(reviews);

        MemberReviewScoreResponse response = peerReviewService.calculateUserReviewScore(revieweeMemberId);

        assertNotNull(response);
        assertEquals(revieweePublicId, response.memberPublicId());
        assertEquals(avgScore, response.overallAverageScore());
        assertEquals(1, response.reviewCount());

        verify(peerReviewRepository, times(1)).calculateOverallAverageScore(revieweeMemberId);
        verify(peerReviewRepository, times(1)).findAllByRevieweeMemberId(revieweeMemberId);
        verify(memberRepository, times(1)).findById(revieweeMemberId);
    }

    @Test
    @DisplayName("사용자 리뷰 점수 계산 - 리뷰 없음")
    void calculateUserReviewScoreNoReviewsSuccess() {
        // given
        given(peerReviewRepository.calculateOverallAverageScore(revieweeMemberId)).willReturn(null);
        given(peerReviewRepository.findAllByRevieweeMemberId(revieweeMemberId)).willReturn(Arrays.asList());

        // when
        MemberReviewScoreResponse response = peerReviewService.calculateUserReviewScore(revieweeMemberId);

        // then
        assertNotNull(response);
        assertEquals(revieweePublicId, response.memberPublicId());
        assertEquals(0.0, response.overallAverageScore());
        assertEquals(0, response.reviewCount());

        verify(peerReviewRepository, times(1)).calculateOverallAverageScore(revieweeMemberId);
        verify(peerReviewRepository, times(1)).findAllByRevieweeMemberId(revieweeMemberId);
        verify(memberRepository, times(1)).findById(revieweeMemberId);
    }

    @Test
    @DisplayName("사용자 리뷰 코멘트 조회 - 성공")
    void getUserReviewCommentsSuccess() {
        List<PeerReview> reviews  = Arrays.asList(testPeerReview);
        given(peerReviewRepository.findAllByRevieweeMemberId(revieweeMemberId)).willReturn(reviews);

        List<MemberReviewCommentResponse> responses = peerReviewService.getUserReviewComments(revieweeMemberId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("좋은 팀원이었습니다.", responses.get(0).reviewComment());
        assertEquals(postId, responses.get(0).postId());
        assertEquals("테스트 프로젝트", responses.get(0).postTitle());
        assertNotNull(responses.get(0).reviewDate());

        verify(peerReviewRepository, times(1)).findAllByRevieweeMemberId(revieweeMemberId);
    }

    @Test
    @DisplayName("사용자 리뷰 코민트 조회 - 리뷰 없음")
    void getUserReviewCommentsNoReviewsSuccess() {
        given(peerReviewRepository.findAllByRevieweeMemberId(revieweeMemberId)).willReturn(Arrays.asList());

        List<MemberReviewCommentResponse> responses = peerReviewService.getUserReviewComments(revieweeMemberId);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(peerReviewRepository, times(1)).findAllByRevieweeMemberId(revieweeMemberId);
    }

}
