package com.eum.post.model.repository;

import com.eum.post.model.entity.PostPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPositionRepository extends JpaRepository<PostPosition, Long> {
    // 게시글 ID로 포지션 정보 조회 메소드
    List<PostPosition> findByPostId(Long postId);

    // 추가: 여러 게시글 ID로 한 번에 조회 (JOIN FETCH 사용)
    @Query("SELECT pp FROM PostPosition pp JOIN FETCH pp.position WHERE pp.post.id IN :postIds")
    List<PostPosition> findByPostIdInWithPosition(@Param("postIds") List<Long> postIds);
}
