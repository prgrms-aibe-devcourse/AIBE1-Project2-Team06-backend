package com.eum.post.model.repository;

import com.eum.post.model.entity.PostMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostMemberRepository extends JpaRepository<PostMember, Long> {
    boolean existsByPostIdAndMemberIdAndIsOwnerTrue(Long postId, Long memberId);

    @Query("SELECT pm FROM PostMember pm JOIN FETCH pm.member WHERE pm.post.id = :postId")
    List<PostMember> findAllWithMemberByPostId(@Param("postId") Long postId);

}
