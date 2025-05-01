package com.eum.post.model.repository;

import com.eum.post.model.entity.PostPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPositionRepository extends JpaRepository<PostPosition, Long> {
}
