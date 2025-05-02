package com.eum.post.sevice;

import com.eum.post.model.dto.response.PostResponse;

public interface PostService {

    PostResponse completePost(Long postId, Long userId, String githubLink);
}
