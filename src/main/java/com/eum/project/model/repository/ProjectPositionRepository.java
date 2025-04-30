package com.eum.project.model.repository;

import com.eum.project.model.entity.ProjectPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPositionRepository extends JpaRepository<ProjectPosition, Long> {
}
