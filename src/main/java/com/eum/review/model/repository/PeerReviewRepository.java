package com.eum.review.model.repository;

import com.eum.review.model.entity.PeerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeerReviewRepository extends JpaRepository<PeerReview, Long> {

    // 특정 사용자기 받은 리뷰 조회
    List<PeerReview> findAllByRevieweeUserId(Long revieweeMemberId);

    // 특정 사용자가 받은 전체 평균 점수
    @Query("SELECT AVG(p.averageScore) FROM PeerReview p WHERE p.revieweeMemberId = :userId")
    Double calculateOverallAverageScore(@Param("userId") Long userId);

    // 특정 프로젝트에서 특정 사용자가 받은 리뷰 목록
    List<PeerReview> findAllByPostIdAndRevieweeMemberId(Long postId, Long revieweeMemberId);
}
