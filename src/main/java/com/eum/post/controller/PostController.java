package com.eum.post.controller;

import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.sevice.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PatchMapping("/{postId}/complete")
    public ResponseEntity<PostResponse> completePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam String githubLink
    ) {
        return ResponseEntity.ok(postService.completePost(postId, userId, githubLink));
    }
}
