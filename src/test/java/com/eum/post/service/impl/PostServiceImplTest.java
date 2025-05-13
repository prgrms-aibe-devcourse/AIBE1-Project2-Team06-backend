package com.eum.post.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.global.model.repository.PositionRepository;
import com.eum.global.model.repository.TechStackRepository;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.PortfolioDto;
import com.eum.post.model.dto.PostDto;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostUpdateResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.*;
import com.eum.post.model.repository.PostMemberRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostMemberRepository postMemberRepository;

    private Post testPost;
    private PostRequest testPostRequest;
    private Position testPosition;
    private TechStack testTechStack;
    private Long testUserId;
    private UUID testPublicId;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        testUserId = 1L;
        testPublicId = UUID.randomUUID();

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

        // Member 객체 모킹 추가
        Member mockMember = mock(Member.class);
        lenient().when(mockMember.getId()).thenReturn(testUserId);
        lenient().when(mockMember.getPublicId()).thenReturn(testPublicId);
        lenient().when(mockMember.getNickname()).thenReturn("테스트 사용자");
        lenient().when(memberRepository.findByPublicId(testPublicId)).thenReturn(Optional.of(mockMember));

        // Post 객체도 Mock으로 변경하여 필요한 메서드 스텁
        testPost = mock(Post.class);
        lenient().when(testPost.getId()).thenReturn(1L);
        lenient().when(testPost.getTitle()).thenReturn("테스트 게시글");
        lenient().when(testPost.getContent()).thenReturn("테스트 내용");
        lenient().when(testPost.getRecruitType()).thenReturn(RecruitType.PROJECT);
        lenient().when(testPost.getRecruitMember()).thenReturn(5);
        lenient().when(testPost.getProgressMethod()).thenReturn(ProgressMethod.ONLINE);
        lenient().when(testPost.getPeriod()).thenReturn(Period.MONTH_3);
        lenient().when(testPost.getDeadline()).thenReturn(LocalDate.now().plusDays(10));
        lenient().when(testPost.getLinkType()).thenReturn(LinkType.KAKAO);
        lenient().when(testPost.getLink()).thenReturn("https://test.com");
        lenient().when(testPost.getMemberPublicId()).thenReturn(testPublicId);
        lenient().when(testPost.getMemberNickname()).thenReturn("테스트 사용자");
        lenient().when(testPost.getStatus()).thenReturn(Status.RECRUITING);
        lenient().when(testPost.getCultureFit()).thenReturn(CultureFit.NONE);
        lenient().when(testPost.getCreatedAt()).thenReturn(LocalDateTime.now());
        lenient().when(testPost.getUpdatedAt()).thenReturn(LocalDateTime.now());
    }

    @Test
    @DisplayName("게시글 생성 - 성공")
    void createPostSuccess() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(testPost);
        given(techStackRepository.findById(1L)).willReturn(Optional.of(testTechStack));
        given(positionRepository.findById(1L)).willReturn(Optional.of(testPosition));

        //when
        PostDto response = postService.create(testPostRequest, testPublicId);

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
                postService.create(testPostRequest, testPublicId));

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
                postService.create(testPostRequest, testPublicId)
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
        PostDto response = postService.findByPostId(postId);

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

        // 업데이트 된 Post를 모킹
        Post updatedPost = mock(Post.class);
        lenient().when(updatedPost.getId()).thenReturn(1L);
        lenient().when(updatedPost.getTitle()).thenReturn("수정된 제목");
        lenient().when(updatedPost.getContent()).thenReturn("수정된 내용");
        lenient().when(updatedPost.getRecruitType()).thenReturn(RecruitType.STUDY);
        lenient().when(updatedPost.getRecruitMember()).thenReturn(3);
        lenient().when(updatedPost.getProgressMethod()).thenReturn(ProgressMethod.OFFLINE);
        lenient().when(updatedPost.getPeriod()).thenReturn(Period.MONTH_5);
        lenient().when(updatedPost.getLinkType()).thenReturn(LinkType.GOOGLE);
        lenient().when(updatedPost.getLink()).thenReturn("http://updated.com");

        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));
        given(testPost.getMemberPublicId()).willReturn(testPublicId);
        given(postTechStackRepository.findByPostId(postId)).willReturn(Arrays.asList());
        given(postPositionRepository.findByPostId(postId)).willReturn(Arrays.asList());
        given(techStackRepository.findById(1L)).willReturn(Optional.of(testTechStack));
        given(positionRepository.findById(1L)).willReturn(Optional.of(testPosition));

        // updatePost 메서드가 호출된 후, 업데이트된 값을 반환하도록 설정
        doAnswer(invocation -> {
            // testPost를 updatedPost로 대체하는 효과
            when(testPost.getTitle()).thenReturn("수정된 제목");
            when(testPost.getContent()).thenReturn("수정된 내용");
            return null;
        }).when(testPost).updatePost(any(PostUpdateResponse.class));

        PostDto response = postService.update(postId, updateRequest, testPublicId);

        assertNotNull(response);
        assertEquals("수정된 제목", response.title());
        assertEquals("수정된 내용", response.content());
        verify(postRepository, times(1)).findById(postId);
        verify(testPost, times(1)).updatePost(any(PostUpdateResponse.class));
    }

    @Test
    @DisplayName("게시글 수정 - 권한 없음 실패")
    void updateNoPermissionFail() {
        Long postId = 1L;
        UUID differentPublicId = UUID.randomUUID();

        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));

        CustomException exception = assertThrows(CustomException.class, () ->
                postService.update(postId, testPostRequest, differentPublicId));

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

        postService.deletePost(postId, testPublicId);

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(testPost);
    }

    @Test
    @DisplayName("게시글 삭제 - 권한 없음 실패")
    void deletePostNoPermissionFail() {
        Long postId = 1L;
        UUID differentPublicId = UUID.randomUUID();

        given(postRepository.findById(postId)).willReturn(Optional.of(testPost));

        CustomException exception = assertThrows(CustomException.class, () ->
                postService.deletePost(postId, differentPublicId));

        assertEquals(ErrorCode.POST_ACCESS_DENIED, exception.getErrorCode());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    @DisplayName("게시글 완료 - 성공")
    void completePostSuccess() {
        Long postId = 1L;
        String githubLink = "https://github.com";

        when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));

        // Member 객체 모킹 추가
        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(testUserId);
        when(memberRepository.findByPublicId(testPublicId)).thenReturn(Optional.of(mockMember));

        when(postTechStackRepository.findByPostId(postId)).thenReturn(Arrays.asList());
        when(postPositionRepository.findByPostId(postId)).thenReturn(Arrays.asList());

        // PortfolioDto 반환값 모킹
        PortfolioDto mockPortfolioDto = mock(PortfolioDto.class);
        when(portfolioService.createPortfolio(any(Member.class), eq(postId), eq(githubLink)))
                .thenReturn(mockPortfolioDto);

        PostDto response = postService.completePost(postId, testPublicId, githubLink);

        assertNotNull(response);
        verify(portfolioService, times(1)).createPortfolio(mockMember, postId, githubLink);
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

        Page<PostDto> responses = postService.findPostsWithFilters(
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

        Page<PostDto> responses = postService.findPostsWithFilters(
                keyword, null, null, null, null, null, null, pageable
        );

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        verify(postRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}
