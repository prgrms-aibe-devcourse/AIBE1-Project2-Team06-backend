package com.eum.project.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Project_Position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    public static ProjectPosition of(Project project, Position position) {
        ProjectPosition projectPositionEntity = new ProjectPosition();
        projectPositionEntity.project = project;
        projectPositionEntity.position = position;
        return projectPositionEntity;
    }

}
