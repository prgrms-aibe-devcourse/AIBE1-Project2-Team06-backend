package com.eum.project.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Project_TechStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectTechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "techStack_id", nullable = false)
    private TechStack techStack;

    public static ProjectTechStack of(Project project, TechStack techStack) {
        ProjectTechStack projectTechStackEntity = new ProjectTechStack();
        projectTechStackEntity.project = project;
        projectTechStackEntity.techStack = techStack;
        return projectTechStackEntity;
    }
}
