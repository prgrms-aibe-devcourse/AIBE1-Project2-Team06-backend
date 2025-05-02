package com.eum.post.service;

import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.Post;

public interface PostService {

    PostResponse create(PostRequest postRequest, Long userId);

    PostResponse getPost(Long postId);
}
