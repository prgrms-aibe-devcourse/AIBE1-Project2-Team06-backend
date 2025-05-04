package com.eum.post.model.repository;

import com.eum.post.model.entity.PostPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPositionRepository extends JpaRepository<PostPosition, Long> {
    // 게시글 ID로 포지션 정보 조회 메소드
    List<PostPosition> findByPostId(Long postId);
}
