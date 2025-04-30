package com.eum.project.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Project_Position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectPositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Column(name = "project_id", nullable = false)
    private ProjectEntity project;

    @ManyToOne
    @Column(name = "position_id", nullable = false)
    private PositionEntity position;

    public static ProjectPositionEntity of(ProjectEntity project, PositionEntity position) {
        ProjectPositionEntity projectPositionEntity = new ProjectPositionEntity();
        projectPositionEntity.project = project;
        projectPositionEntity.position = position;
        return projectPositionEntity;
    }

}
