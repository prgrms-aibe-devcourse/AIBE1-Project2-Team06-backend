package com.eum.post.model.repository;

import com.eum.post.model.entity.PostPosition;
import com.eum.post.model.entity.PostTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTechStackRepository extends JpaRepository<PostTechStack, Long> {
    // 게시글 ID로 기술스택 정보 조회 메소드
    List<PostTechStack> findByPostId(Long postId);
}
