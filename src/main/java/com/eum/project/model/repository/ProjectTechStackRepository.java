package com.eum.project.model.repository;

import com.eum.project.model.entity.ProjectTechStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStackEntity, Long> {
}
