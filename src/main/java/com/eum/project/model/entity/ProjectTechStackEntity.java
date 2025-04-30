package com.eum.project.model.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Project_TechStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectTechStackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "project_id", nullable = false)
    private ProjectEntity project;

    @ManyToOne
    @Column(name = "techStack_id", nullable = false)
    private TechStackEntity techStack;

    public static ProjectTechStackEntity of(ProjectEntity project, TechStackEntity techStack) {
        ProjectTechStackEntity projectTechStackEntity = new ProjectTechStackEntity();
        projectTechStackEntity.project = project;
        projectTechStackEntity.techStack = techStack;
        return projectTechStackEntity;
    }
}
