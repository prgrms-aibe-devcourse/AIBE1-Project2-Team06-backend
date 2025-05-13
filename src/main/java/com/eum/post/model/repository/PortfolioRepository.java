package com.eum.post.model.repository;

import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.enumerated.RecruitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("SELECT p FROM Portfolio p WHERE p.member.id = :userId")
    List<Portfolio> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Portfolio p WHERE p.member.id = :userId AND p.recruitType = :recruitType")
    List<Portfolio> findAllByUserIdAndRecruitType(@Param("userId") Long userId, @Param("recruitType") RecruitType recruitType);
}
