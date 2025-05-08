package com.eum.member.model.repository;

import com.eum.member.model.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByAuthIdAndProvider(String authId, String provider);

    // 닉네임으로 멤버 찾기
    Optional<Member> findByNickname(String nickname);

    //PostMemeberSerivceImpl에서 사용
    List<Member> findAllByNicknameIn(List<String> allNicknames);
    Optional<Member> findByPublicId(UUID publicId);

    UUID publicId(UUID publicId);
}
