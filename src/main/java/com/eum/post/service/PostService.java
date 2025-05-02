package com.eum.post.service;

import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponse create(PostRequest postRequest, Long userId);

    PostResponse findByPostId(Long postId); // 개별만 조회

    Page<PostResponse> findByAllPosts(Pageable pageable);

    PostResponse completePost(Long postId, Long userId, String githubLink);
}
