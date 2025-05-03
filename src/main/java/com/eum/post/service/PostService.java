package com.eum.post.service;

import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostResponse create(PostRequest postRequest, Long userId);

    PostResponse findByPostId(Long postId); // 개별만 조회

    Page<PostResponse> findByAllPosts(Pageable pageable);

    Page<PostResponse> findAllByRecruitType(RecruitType recruitType, Pageable pageable);

    PostResponse completePost(Long postId, Long userId, String githubLink);

    // 필터링 기능 추가
    Page<PostResponse> findPostsWithFilters(
            RecruitType recruitType,
            ProgressMethod progressMethod,
            CultureFit cultureFit,
            Long positionId,
            List<Long> techStackIds,
            Pageable pageable
    );

}
