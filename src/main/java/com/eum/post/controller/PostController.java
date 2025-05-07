package com.eum.post.controller;

import com.eum.post.model.dto.request.PostFilterRequest;
import com.eum.post.model.dto.request.GithubLinkRequest;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 개별 조회 API
     *
     * @param postId 게시글 ID
     * @return 조회된 게시글 정보
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse response = postService.findByPostId(postId);
        return ResponseEntity.ok(response);
    }

    /**
     * 전체 글 조회 및 필터링 API
     * @param filter
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(
            PostFilterRequest filter,
            @PageableDefault(page = 0, size = 8, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // Enum 변환
        RecruitType recruitTypeEnum = RecruitType.fromString(filter.recruitType());
        ProgressMethod progressMethodEnum = ProgressMethod.fromString(filter.progressMethod());
        CultureFit cultureFitEnum = CultureFit.fromString(filter.cultureFit());

        // 서비스 호출
        Page<PostResponse> responses = postService.findPostsWithFilters(
                filter.keyword(),
                recruitTypeEnum,
                progressMethodEnum,
                cultureFitEnum,
                filter.positionId(),
                filter.techStackIds(),
                pageable);
        return ResponseEntity.ok(responses);
    }

    /**
     * 게시글 등록 API
     * @return 등록된 게시글 정보
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostRequest request,
            @RequestHeader("X-USER-ID") Long userId
        ) {
        PostResponse response = postService.create(request, userId);
        // 201 Created 상태코드와 함께 응답
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 게시글 삭제 API
     *
     * @param postId
     * @param userId
     * @return
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content 상태코드 반환
    }

    /**
     * 게시글 수정 API
     *
     * @param postId
     * @param request
     * @param userId
     * @return
     */
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest request,
            @RequestHeader("X-USER-ID") Long userId) {
        PostResponse response = postService.update(postId, request, userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}/complete")
    public ResponseEntity<PostResponse> completePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody GithubLinkRequest request) {
        return ResponseEntity.ok(postService.completePost(postId, userId, request.githubLink()));
    }
}
