package com.eum.post.sevice;

import com.eum.post.model.dto.response.PostResponse;

public interface ProjectService {

    PostResponse completeProject(Long projectId, Long userId, String githubLink);
}
