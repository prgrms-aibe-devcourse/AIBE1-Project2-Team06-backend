package com.eum.post.model.repository;

import com.eum.post.model.entity.Portfolio;
import com.eum.post.model.entity.enumerated.RecruitType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findAllByMemberId(Long userId);

    List<Portfolio> findAllByMemberIdAndRecruitType(Long userId, RecruitType recruitType);
}
