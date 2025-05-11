package com.eum.post.service;

import com.eum.post.model.dto.PostMemberDto;
import com.eum.post.model.dto.response.PostMemberResponse;

import java.util.List;

public interface PostMemberService {
    /**
     * 게시글 멤버 목록을 덮어쓰기 방식으로 업데이트합니다.
     * 소유자(모집자)는 항상 유지됩니다.
     *
     * @param postId 게시글 ID
     * @param nicknames 멤버 닉네임 목록
     * @param ownerId 소유자(모집자) ID
     * @return 업데이트된 멤버 목록
     */
    //List<PostMemberResponse> updateMembers(Long postId, List<String> nicknames, Long ownerId);
    List<PostMemberDto> updateMembers(Long postId, List<String> nicknames, Long ownerId);

    /**
     * 게시글 멤버 목록을 조회합니다.
     *
     * @param postId 게시글 ID
     * @return 멤버 목록
     */
    //List<PostMemberResponse> getPostMembers(Long postId);
    List<PostMemberDto> getPostMembers(Long postId);

    /**
     * 특정 사용자가 게시글의 소유자(모집자)인지 확인합니다.
     *
     * @param postId 게시글 ID
     * @param memberId 확인할 멤버 ID
     * @return 소유자 여부
     */
    boolean isOwner(Long postId, Long memberId);
}
