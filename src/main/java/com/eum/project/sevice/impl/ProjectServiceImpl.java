package com.eum.project.sevice.impl;

import com.eum.project.model.dto.response.ProjectResponse;
import com.eum.project.model.entity.Project;
import com.eum.project.model.entity.enumerated.Status;
import com.eum.project.model.repository.ProjectRepository;
import com.eum.project.sevice.PortfolioService;
import com.eum.project.sevice.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final PortfolioService portfolioService;

    @Transactional
    @Override
    public ProjectResponse completeProject(Long projectId, Long userId, String githubLink) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

        project.updateStatus(Status.COMPLETED);

        portfolioService.createPortfolio(userId, projectId, githubLink);

        // TechStack과 Position 가져오는 로직 필요
        List<ProjectResponse.TechStackResponse> techStacks = new ArrayList<>();
        List<ProjectResponse.PositionResponse> positions = new ArrayList<>();
        return ProjectResponse.from(project, techStacks, positions);
    }
}
