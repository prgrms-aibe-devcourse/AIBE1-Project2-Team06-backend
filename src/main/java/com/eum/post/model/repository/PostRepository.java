package com.eum.post.model.repository;

import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.RecruitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>{
    // RecruitType으로 조회하는 메소드
    Page<Post> findAllByRecruitType(RecruitType recruitType, Pageable pageable);
}
