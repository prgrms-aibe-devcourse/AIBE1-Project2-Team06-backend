package com.eum.member.model.repository;

import com.eum.member.model.entity.MemberPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPositionRepository extends JpaRepository<MemberPosition, Long> {
}
