package com.eum.post.model.repository;

import com.eum.post.model.entity.PostMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostMemberRepository extends JpaRepository<PostMember, Long> {
    List<PostMember> findByPostId(Long postId);
    Optional<PostMember> findByPostIdAndMemberId(Long postId, Long memberId);
    boolean existsByPostIdAndMemberIdAndIsOwnerTrue(Long postId, Long memberId);
}
