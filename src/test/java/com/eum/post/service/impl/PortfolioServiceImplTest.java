package com.eum.post.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.member.model.entity.Member;
import com.eum.post.model.dto.PortfolioDto;
import com.eum.post.model.dto.response.PortfolioResponse;
import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.model.repository.PortfolioRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.review.model.repository.PeerReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceImplTest {

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private PeerReviewRepository peerReviewRepository;

    // 테스트 데이터
    private Member mockMember;
    private Long userId;
    private Long postId;
    private String link;
    private String postTitle;
    private Post testPost;
    private Portfolio savedPortfolio;
    private Double averageScore;
    private RecruitType recruitType;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        userId = 1L;
        postId = 2L;
        link = "https://github.com/testproject";
        postTitle = "테스트 프로젝트";
        averageScore = 4.5;
        recruitType = RecruitType.PROJECT;

        // Mock 객체 생성
        mockMember = mock(Member.class);
        lenient().when(mockMember.getId()).thenReturn(userId);

        testPost = mock(Post.class);
        lenient().when(testPost.getId()).thenReturn(postId);
        lenient().when(testPost.getTitle()).thenReturn(postTitle);
        lenient().when(testPost.getRecruitType()).thenReturn(recruitType);

        savedPortfolio = mock(Portfolio.class);
        lenient().when(savedPortfolio.getId()).thenReturn(1L);
        lenient().when(savedPortfolio.getMember()).thenReturn(mockMember);
        lenient().when(savedPortfolio.getPostId()).thenReturn(postId);
        lenient().when(savedPortfolio.getPostTitle()).thenReturn(postTitle);
        lenient().when(savedPortfolio.getPostLink()).thenReturn(link);
        lenient().when(savedPortfolio.getAverageScore()).thenReturn(averageScore);
        lenient().when(savedPortfolio.getRecruitType()).thenReturn(recruitType);
    }

    @Test
    @DisplayName("포트폴리오 생성 - 성공 (리뷰 점수 있음)")
    void createPortfolioWithScoreSuccess() {
        // given
        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));
        given(peerReviewRepository.calculateOverallAverageScore(userId)).willReturn(averageScore);
        given(portfolioRepository.save(any(Portfolio.class))).willReturn(savedPortfolio);

        // when
        PortfolioDto result = portfolioService.createPortfolio(mockMember, postId, link);

        // then
        assertNotNull(result);
        assertEquals(userId, result.useId());
        assertEquals(postId, result.postId());
        assertEquals(postTitle, result.postTitle());
        assertEquals(link, result.postLink());
        assertEquals(averageScore, result.averageScore());

        // 저장소 호출 검증
        verify(postRepository, times(1)).findById(postId);
        verify(peerReviewRepository, times(1)).calculateOverallAverageScore(userId);
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    @DisplayName("포트폴리오 생성 - 성공 (리뷰 점수 없음)")
    void createPortfolioNoScoreSuccess() {
        // given
        Portfolio savedPortfolioNoScore = mock(Portfolio.class);
        when(savedPortfolioNoScore.getId()).thenReturn(1L);
        when(savedPortfolioNoScore.getMember()).thenReturn(mockMember);
        when(savedPortfolioNoScore.getPostId()).thenReturn(postId);
        when(savedPortfolioNoScore.getPostTitle()).thenReturn(postTitle);
        when(savedPortfolioNoScore.getPostLink()).thenReturn(link);
        when(savedPortfolioNoScore.getAverageScore()).thenReturn(0.0);
        when(savedPortfolioNoScore.getRecruitType()).thenReturn(recruitType);

        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));
        given(peerReviewRepository.calculateOverallAverageScore(userId)).willReturn(null);
        given(portfolioRepository.save(any(Portfolio.class))).willReturn(savedPortfolioNoScore);

        // when
        PortfolioDto result = portfolioService.createPortfolio(mockMember, postId, link);

        // then
        assertNotNull(result);
        assertEquals(0.0, result.averageScore());

        verify(postRepository, times(1)).findById(postId);
        verify(peerReviewRepository, times(1)).calculateOverallAverageScore(userId);
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

    @Test
    @DisplayName("포트폴리오 생성 - 실패 (게시글 없음)")
    void createPortfolioPostNotFoundFail() {
        // given
        mockMember = mock(Member.class);

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            portfolioService.createPortfolio(mockMember, postId, link);
        });

        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());

        verify(postRepository, times(1)).findById(postId);
        verify(peerReviewRepository, never()).calculateOverallAverageScore(anyLong());
        verify(portfolioRepository, never()).save(any(Portfolio.class));
    }

    @Test
    @DisplayName("사용자 포트폴리오 목록 조회")
    void getUserPortfoliosSuccess() {
        // given
        List<Portfolio> mockPortfolios = List.of(savedPortfolio);
        given(portfolioRepository.findAllByUserId(userId)).willReturn(mockPortfolios);  // 이 부분 수정

        // when
        List<PortfolioResponse> result = portfolioService.getUserPortfolios(userId);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(portfolioRepository, times(1)).findAllByUserId(userId);
    }
}
