package com.eum.post.service;

import com.eum.post.model.dto.PostDto;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.model.entity.enumerated.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PostService {

    PostDto create(PostRequest postRequest, UUID publicId);

    PostDto findByPostId(Long postId); // 개별만 조회

    PostDto update(Long postId, PostRequest postRequest, UUID publicId);

    void deletePost(Long postId, UUID publicId);

    PostDto completePost(Long postId, UUID publicId, String githubLink) ;

    // 필터링 기능 추가
    Page<PostDto> findPostsWithFilters(
            String keyword,
            RecruitType recruitType,
            ProgressMethod progressMethod,
            CultureFit cultureFit,
            Boolean isRecruiting,
            Long positionId,
            List<Long> techStackIds,
            Pageable pageable
    );
}
