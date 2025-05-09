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

    //PostMemeberSerivceImpl에서 사용
    List<Member> findAllByNicknameIn(List<String> allNicknames);
    Optional<Member> findByPublicId(UUID publicId);

}
