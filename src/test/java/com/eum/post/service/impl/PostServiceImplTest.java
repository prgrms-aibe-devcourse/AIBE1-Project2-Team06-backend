package com.eum.post.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.global.model.repository.PositionRepository;
import com.eum.global.model.repository.TechStackRepository;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.LinkType;
import com.eum.post.model.entity.enumerated.Period;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.model.repository.PostPositionRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.post.model.repository.PostTechStackRepository;
import com.eum.post.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostTechStackRepository postTechStackRepository;

    @Mock
    private PostPositionRepository postPositionRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private TechStackRepository techStackRepository;

    @Mock
    private PortfolioService portfolioService;

    private Post testPost;
    private PostRequest testPostRequest;
    private Position testPosition;
    private TechStack testTechStack;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        testUserId = 1L;

        testPosition = Position.of("백엔드");
        testTechStack = TechStack.of("Java");

        testPostRequest = new PostRequest(
                "테스트 게시글",
                "테스트 내용",
                RecruitType.PROJECT,
                5,
                ProgressMethod.ONLINE,
                Period.MONTH_3,
                LocalDate.now().plusDays(10),
                LinkType.KAKAO,
                "https://test.com",
                Arrays.asList(1L),
                Arrays.asList(1L)
        );

        testPost = testPostRequest.toEntity(testUserId);
        setPostId(testPost, 1L);
    }

    // 유틸리티 메서드
    private void setPostId(Post post, Long id) {
        try {
            Field idField = Post.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(post, id);
        } catch (Exception e) {
            throw new RuntimeException("ID 설정 실패", e);
        }
    }

    @Test
    @DisplayName("게시글 생성 - 성공")
    void createPostSuccess() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(testPost);
        given(techStackRepository.findById(1L)).willReturn(Optional.of(testTechStack));
        given(positionRepository.findById(1L)).willReturn(Optional.of(testPosition));

        //when
        PostResponse response = postService.create(testPostRequest, testUserId);

        // then
        assertNotNull(response);
        assertEquals(testPost.getTitle(), response.title());
        assertEquals(testPost.getContent(), response.content());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postTechStackRepository, times(1)).save(any());
        verify(postPositionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("게시글 생성 - 기술스택 없음 실패")
    void createTechStackNotFoundFail() {
        //given
        given(postRepository.save(any(Post.class))).willReturn(testPost);
        given(techStackRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () ->
                postService.create(testPostRequest, testUserId));

        verify(postRepository, times(1)).save(any(Post.class));
        verify(techStackRepository, times(1)).findById(1L);

    }

    @Test
    @DisplayName("게시글 생성 - 포지션 없음 실패")
    void createPositionNotFoundFail() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(testPost);
        given(techStackRepository.findById(1L)).willReturn(Optional.of(testTechStack));
        given(positionRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () ->
                postService.create(testPostRequest, testUserId)
        );

        verify(postRepository, times(1)).save(any(Post.class));
        verify(techStackRepository, times(1)).findById(1L);
        verify(positionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("게시글 조회 - 성공")
    void findByPostIdSuccess() {
        // given
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));
        given(postTechStackRepository.findByPostId(postId)).willReturn(Arrays.asList());
        given(postPositionRepository.findByPostId(postId)).willReturn(Arrays.asList());

        // when
        PostResponse response = postService.findByPostId(postId);

        // then
        assertNotNull(response);
        assertEquals(testPost.getTitle(), response.title());
        assertEquals(testPost.getContent(), response.content());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 조회 - 게시글 없음 실패")
    void findByPostIdPostNotFoundFail() {
        // given
        Long postId = 999L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                postService.findByPostId(postId));

        assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
        verify(postRepository, times(1)).findById(postId);
    }
}
