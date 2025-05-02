package com.eum.post.controller;

import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
     * 게시글 목록 조회 API
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param sort 정렬 기준 (예: createdAt,desc)
     * @return 게시글 목록
     */
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        // 정렬 정보 파싱
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        // 페이지네이션 및 정렬 객체 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // 서비스 호출
        Page<PostResponse> responses = postService.findByAllPosts(pageable);

        return ResponseEntity.ok(responses);
    }

    /**
     * 게시글 등록 API
     * @return 등록된 게시글 정보
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        //TODO: userId 받아오는 부분 개발
        Long userId = 1L;

        PostResponse response = postService.create(request, userId);
        // 201 Created 상태코드와 함께 응답
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{postId}/complete")
    public ResponseEntity<PostResponse> completePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody String githubLink) {
        return ResponseEntity.ok(postService.completePost(postId, userId, githubLink));
    }
}
