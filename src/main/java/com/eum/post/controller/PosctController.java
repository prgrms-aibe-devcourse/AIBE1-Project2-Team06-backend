package com.eum.post.controller;

import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.sevice.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class PosctController {

    private final PostService postService;

    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<PostResponse> completeProject(
            @PathVariable Long projectId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam String githubLink
    ) {
        return ResponseEntity.ok(postService.completeProject(projectId, userId, githubLink));
    }
}
