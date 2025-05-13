package com.eum.post.model.repository;

import com.eum.post.model.entity.PostTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTechStackRepository extends JpaRepository<PostTechStack, Long> {
    // 게시글 ID로 기술스택 정보 조회 메소드
    List<PostTechStack> findByPostId(Long postId);

    // 추가: 여러 게시글 ID로 한 번에 조회 (JOIN FETCH 사용)
    @Query("SELECT pts FROM PostTechStack pts JOIN FETCH pts.techStack WHERE pts.post.id IN :postIds")
    List<PostTechStack> findByPostIdInWithTechStack(@Param("postIds") List<Long> postIds);
}
