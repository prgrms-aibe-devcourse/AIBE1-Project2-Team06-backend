package com.eum.post.controller;

import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.service.PostService;
import lombok.RequiredArgsConstructor;
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
}
