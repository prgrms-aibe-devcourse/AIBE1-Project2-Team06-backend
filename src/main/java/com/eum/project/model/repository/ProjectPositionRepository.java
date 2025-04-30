package com.eum.project.model.repository;

import com.eum.project.model.entity.ProjectPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPositionRepository extends JpaRepository<ProjectPositionEntity, Long> {
}
