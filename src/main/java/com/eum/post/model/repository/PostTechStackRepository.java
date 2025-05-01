package com.eum.post.model.repository;

import com.eum.post.model.entity.PostTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTechStackRepository extends JpaRepository<PostTechStack, Long> {
}
