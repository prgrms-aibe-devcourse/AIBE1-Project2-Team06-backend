package com.eum.review.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.post.model.entity.Post;
import com.eum.post.model.repository.PostRepository;
import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.UserReviewCommentResponse;
import com.eum.review.model.dto.response.UserReviewScoreResponse;
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

    private Post testPost;
    private PeerReview testPeerReview;
    private PeerReviewCreateRequest testRequest;
    private Long reviewerUserId;
    private Long revieweeUserId;
    private Long postId;

    @BeforeEach
    void setUp() {
        reviewerUserId = 1L;
        revieweeUserId = 2L;
        postId = 1L;

        testPost = mock(Post.class);
        lenient().when(testPost.getId()).thenReturn(postId);

        testRequest = new PeerReviewCreateRequest(
                postId,
                revieweeUserId,
                5,
                4,
                5,
                "좋은 팀원이였습니다."
        );

        testPeerReview = mock(PeerReview.class);
        lenient().when(testPeerReview.getId()).thenReturn(1);
        lenient().when(testPeerReview.getReviewerUserId()).thenReturn(reviewerUserId);
        lenient().when(testPeerReview.getRevieweeUserId()).thenReturn(revieweeUserId);
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
        given(peerReviewRepository.save(any(PeerReview.class))).willReturn(testPeerReview);

        PeerReviewResponse response = peerReviewService.createReview(testRequest, reviewerUserId);

        assertNotNull(response);
        assertEquals(testPeerReview.getId(), response.id());
        assertEquals(reviewerUserId, response.reviewerUserId());
        assertEquals(revieweeUserId, response.revieweeUserId());
        assertEquals(testPeerReview.getAverageScore(), response.averageScore());
        assertEquals("좋은 팀원이었습니다.", response.reviewComment());

        verify(postRepository, times(1)).findById(postId);
        verify(peerReviewRepository, times(1)).save(any(PeerReview.class));
    }

    @Test
    @DisplayName("리뷰 생성 - 자기 자신 리뷰 실패")
    void createReviewSelfReviewFail() {
        Long sameUserId = 1L;
        PeerReviewCreateRequest selfReviewRequest = new PeerReviewCreateRequest(
                postId,
                sameUserId,
                5, 4, 5,
                "자기 자신 리뷰"
        );

        CustomException exception = assertThrows(CustomException.class, () ->
                peerReviewService.createReview(selfReviewRequest, sameUserId));

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
                peerReviewService.createReview(testRequest, reviewerUserId)
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

        given(peerReviewRepository.calculateOverallAverageScore(revieweeUserId)).willReturn(avgScore);
        given(peerReviewRepository.findAllByRevieweeUserId(revieweeUserId)).willReturn(reviews);

        UserReviewScoreResponse response = peerReviewService.calculateUserReviewScore(revieweeUserId);

        assertNotNull(response);
        assertEquals(revieweeUserId, response.userId());
        assertEquals(avgScore, response.overallAverageScore());
        assertEquals(1, response.reviewCount());

        verify(peerReviewRepository, times(1)).calculateOverallAverageScore(revieweeUserId);
        verify(peerReviewRepository, times(1)).findAllByRevieweeUserId(revieweeUserId);
    }

    @Test
    @DisplayName("사용자 리뷰 점수 계산 - 리뷰 없음")
    void calculateUserReviewScoreNoReviewsSuccess() {
        // given
        given(peerReviewRepository.calculateOverallAverageScore(revieweeUserId)).willReturn(null);
        given(peerReviewRepository.findAllByRevieweeUserId(revieweeUserId)).willReturn(Arrays.asList());

        // when
        UserReviewScoreResponse response = peerReviewService.calculateUserReviewScore(revieweeUserId);

        // then
        assertNotNull(response);
        assertEquals(revieweeUserId, response.userId());
        assertEquals(0.0, response.overallAverageScore());
        assertEquals(0, response.reviewCount());

        verify(peerReviewRepository, times(1)).calculateOverallAverageScore(revieweeUserId);
        verify(peerReviewRepository, times(1)).findAllByRevieweeUserId(revieweeUserId);
    }

    @Test
    @DisplayName("사용자 리뷰 코멘트 조회 - 성공")
    void getUserReviewCommentsSuccess() {
        List<PeerReview> reviews  = Arrays.asList(testPeerReview);
        given(peerReviewRepository.findAllByRevieweeUserId(revieweeUserId)).willReturn(reviews);

        List<UserReviewCommentResponse> responses = peerReviewService.getUserReviewComments(revieweeUserId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("좋은 팀원이었습니다.", responses.get(0).reviewComment());
        assertEquals(testPost, responses.get(0).post());
        assertNotNull(responses.get(0).reviewDate());

        verify(peerReviewRepository, times(1)).findAllByRevieweeUserId(revieweeUserId);
    }

    @Test
    @DisplayName("사용자 리뷰 코민트 조회 - 리뷰 없음")
    void getUserReviewCommentsNoReviewsSuccess() {
        given(peerReviewRepository.findAllByRevieweeUserId(revieweeUserId)).willReturn(Arrays.asList());

        List<UserReviewCommentResponse> responses = peerReviewService.getUserReviewComments(revieweeUserId);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(peerReviewRepository, times(1)).findAllByRevieweeUserId(revieweeUserId);
    }

}
