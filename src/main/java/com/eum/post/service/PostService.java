package com.eum.post.service;

import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.model.entity.enumerated.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PostService {

    PostResponse create(PostRequest postRequest, Long userId);

    PostResponse findByPostId(Long postId); // 개별만 조회

    PostResponse update(Long postId, PostRequest postRequest, Long userId);

    void deletePost(Long postId, Long userId);

    PostResponse completePost(Long postId, Long userId, String githubLink);

    // 필터링 기능 추가
    Page<PostResponse> findPostsWithFilters(
            String keyword,
            RecruitType recruitType,
            ProgressMethod progressMethod,
            CultureFit cultureFit,
            Long positionId,
            List<Long> techStackIds,
            Pageable pageable
    );

}
