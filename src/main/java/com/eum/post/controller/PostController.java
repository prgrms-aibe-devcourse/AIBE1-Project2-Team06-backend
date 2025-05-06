package com.eum.post.controller;

import com.eum.post.model.dto.PostFilterDto;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(PostFilterDto filter) {
        // 정렬 정보 파싱
        String[] sortParams = filter.getSortValue().split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        // 페이지네이션 및 정렬 객체 생성
        Pageable pageable = PageRequest.of(filter.getPageValue(), filter.getSizeValue(), Sort.by(direction, sortField));

        // DTO의 변환 메서드 사용
        RecruitType recruitTypeEnum = filter.getRecruitTypeEnum();
        ProgressMethod progressMethodEnum = filter.getProgressMethodEnum();
        CultureFit cultureFitEnum = filter.getCultureFitEnum();

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
            @RequestBody PostRequest request) {

        //TODO: userId 받아오는 부분 개발
        Long userId = 2L;

        PostResponse response = postService.create(request, userId);
        // 201 Created 상태코드와 함께 응답
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId) {
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content 상태코드 반환
    }

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
            @RequestBody String githubLink) {
        return ResponseEntity.ok(postService.completePost(postId, userId, githubLink));
    }


}
