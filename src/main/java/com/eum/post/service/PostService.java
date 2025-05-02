package com.eum.post.service;

import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;

public interface PostService {

    PostResponse create(PostRequest postRequest, Long userId);

    PostResponse findByPostId(Long postId);
}
