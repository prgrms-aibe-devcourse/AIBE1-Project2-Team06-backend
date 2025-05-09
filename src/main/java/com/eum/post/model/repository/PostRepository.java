package com.eum.post.model.repository;

import com.eum.member.model.entity.Member;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>{
    // 마감일이 지난 게시글의 상태 업데이트 (RECRUITING → CLOSED)
    @Modifying
    @Query("UPDATE Post p SET p.status = :newStatus WHERE p.status = :currentStatus AND p.deadline < :date")
    int bulkUpdateStatusForExpiredPosts(Status currentStatus, Status newStatus, LocalDate date);
}
