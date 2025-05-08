package com.eum.member.model.repository;

import com.eum.member.model.entity.MemberTechStack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberTechStackRepository extends JpaRepository<MemberTechStack, Long> {}
