package com.eum.post.controller;

import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.sevice.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<PostResponse> completeProject(
            @PathVariable Long projectId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam String githubLink
    ) {
        return ResponseEntity.ok(projectService.completeProject(projectId, userId, githubLink));
    }
}
