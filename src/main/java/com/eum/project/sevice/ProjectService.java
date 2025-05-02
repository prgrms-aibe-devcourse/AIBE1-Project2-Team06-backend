package com.eum.project.sevice;

import com.eum.project.model.dto.response.ProjectResponse;

public interface ProjectService {

    ProjectResponse completeProject(Long projectId, Long userId, String githubLink);
}
