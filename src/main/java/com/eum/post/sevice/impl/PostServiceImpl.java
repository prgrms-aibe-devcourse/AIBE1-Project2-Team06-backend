package com.eum.post.sevice.impl;

import com.eum.post.model.dto.PositionDto;
import com.eum.post.model.dto.PostDto;
import com.eum.post.model.dto.TechStackDto;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.ProjectRepository;
import com.eum.post.sevice.PortfolioService;
import com.eum.post.sevice.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final ProjectRepository projectRepository;
    private final PortfolioService portfolioService;

    @Transactional
    @Override
    public PostResponse completeProject(Long postId, Long userId, String githubLink) {
        Post post = projectRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        post.updateStatus(Status.COMPLETED);

        portfolioService.createPortfolio(userId, postId, githubLink);

        // 추후 가져오는 로직 연결 필요
        List<TechStackDto> techStackDtos = new ArrayList<>();
        List<PositionDto> positionDtos = new ArrayList<>();

        PostDto postDto = PostDto.from(post, techStackDtos, positionDtos);
        return PostResponse.from(postDto);
    }
}
