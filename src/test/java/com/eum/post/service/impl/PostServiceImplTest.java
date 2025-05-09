package com.eum.post.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.global.model.repository.PositionRepository;
import com.eum.global.model.repository.TechStackRepository;
import com.eum.member.model.entity.Member;
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
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

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
    //private Long testUserId;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        //testUserId = 1L;
        testUserId = UUID.fromString("5f8c7b9a-6e4d-4f8c-a1b2-3c4d5e6f7a8b");

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

        // Member 객체 Mock 생성
        Member testMember = mock(Member.class);
        when(testMember.getPublicId()).thenReturn(testUserId);
        // 테스트 Post 엔티티 생성
        testPost = testPostRequest.toEntity(testMember);
        setPostId(testPost, 1L);

//        testPost = testPostRequest.toEntity(testUserId);
//        setPostId(testPost, 1L);
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

    @Test
    @DisplayName("게시글 수정 - 성공")
    void updateSuccess() {
        Long postId = 1L;
        PostRequest updateRequest = new PostRequest(
                "수정된 제목",
                "수정된 내용",
                RecruitType.STUDY,
                3,
                ProgressMethod.OFFLINE,
                Period.MONTH_5,
                LocalDate.now().plusDays(3),
                LinkType.GOOGLE,
                "http://updated.com",
                Arrays.asList(1L),
                Arrays.asList(1L)
        );

        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));
        given(postTechStackRepository.findByPostId(postId)).willReturn(Arrays.asList());
        given(postPositionRepository.findByPostId(postId)).willReturn(Arrays.asList());
        given(techStackRepository.findById(1L)).willReturn(Optional.of(testTechStack));
        given(positionRepository.findById(1L)).willReturn(Optional.of(testPosition));

        PostResponse response = postService.update(postId, updateRequest, testUserId);

        assertNotNull(response);
        assertEquals("수정된 제목", response.title());
        assertEquals("수정된 내용", response.content());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 수정 - 권한 없음 실패")
    void updateNoPermissionFail() {
        Long postId = 1L;
        //Long differentUserId = 999L;
        UUID differentUserId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");  // 다른 UUID 사용


        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));

        CustomException exception = assertThrows(CustomException.class, () ->
                postService.update(postId, testPostRequest, differentUserId));

        assertEquals(ErrorCode.POST_ACCESS_DENIED, exception.getErrorCode());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 삭제 - 성공")
    void deletePostSuccess() {
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));
        given(postTechStackRepository.findByPostId(postId)).willReturn(Arrays.asList());
        given(postPositionRepository.findByPostId(postId)).willReturn(Arrays.asList());

        postService.deletePost(postId, testUserId);

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(testPost);
    }

    @Test
    @DisplayName("게시글 삭제 - 권한 없음 실패")
    void deletePostNoPermissionFail() {
        Long postId = 1L;
        //Long differentUserId = 999L;
        UUID differentUserId = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");  // 다른 UUID 사용


        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));

        CustomException exception = assertThrows(CustomException.class, () ->
                postService.deletePost(postId, differentUserId));

        assertEquals(ErrorCode.POST_ACCESS_DENIED, exception.getErrorCode());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    @DisplayName("게시글 완료 - 성공")
    void completePostSuccess() {
        Long postId = 1L;
        String githubLink = "https://github.com";

        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));
        given(postTechStackRepository.findByPostId(postId)).willReturn(Arrays.asList());
        given(postPositionRepository.findByPostId(postId)).willReturn(Arrays.asList());

        PostResponse response = postService.completePost(postId, testUserId, githubLink);

        assertNotNull(response);
        verify(portfolioService, times(1)).createPortfolio(testUserId, postId, githubLink);
    }

    @Test
    @DisplayName("게시글 목록 조회 - 필터 없음")
    void findPostsWithFiltersNoFilterSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = Arrays.asList(testPost);
        Page<Post> postPage = new PageImpl<>(posts, pageable, 1);

        given(postRepository.findAll(any(Specification.class), eq(pageable))).willReturn(postPage);
        given(postTechStackRepository.findByPostId(any())).willReturn(Arrays.asList());
        given(postPositionRepository.findByPostId(any())).willReturn(Arrays.asList());

        Page<PostResponse> responses = postService.findPostsWithFilters(
                null, null, null, null, null, null, null, pageable
        );

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        verify(postRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("게시글 목록 조회 - 키워드 필터")
    void findPostWithFiltersWithKeywordSuccess() {
        String keyword = "테스트";
        Pageable pageable = PageRequest.of(0, 10);
        List<Post> posts = Arrays.asList(testPost);
        Page<Post> postPage = new PageImpl<>(posts, pageable, 1);

        given(postRepository.findAll(any(Specification.class), eq(pageable))).willReturn(postPage);
        given(postTechStackRepository.findByPostId(any())).willReturn(Arrays.asList());
        given(postPositionRepository.findByPostId(any())).willReturn(Arrays.asList());

        Page<PostResponse> responses = postService.findPostsWithFilters(
                keyword, null, null, null, null, null, null, pageable
        );

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        verify(postRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}
