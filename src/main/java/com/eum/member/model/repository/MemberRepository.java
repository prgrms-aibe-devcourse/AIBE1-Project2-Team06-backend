package com.eum.member.model.repository;

import com.eum.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByAuthIdAndProvider(String authId, String provider);

    // 닉네임으로 멤버 찾기
    Optional<Member> findByNickname(String nickname);
}
