package com.eum.project.controller;

import com.eum.project.model.dto.response.ProjectResponse;
import com.eum.project.sevice.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PatchMapping("/{projectId}/complete")
    public ResponseEntity<ProjectResponse> completeProject(
            @PathVariable Long projectId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam String githubLink
    ) {
        return ResponseEntity.ok(projectService.completeProject(projectId, userId, githubLink));
    }
}
