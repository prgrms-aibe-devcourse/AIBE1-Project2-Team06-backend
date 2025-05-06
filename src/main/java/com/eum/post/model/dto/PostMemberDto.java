package com.eum.post.model.dto;

import com.eum.post.model.entity.PostMember;

import java.time.LocalDateTime;

public record PostMemberDto(
        Long id,
        Long postId,
        Long memberId,
        String nickname,
        Boolean isOwner,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostMemberDto from(PostMember postMember) {
        return new PostMemberDto(
                postMember.getId(),
                postMember.getPost().getId(),
                postMember.getMember().getId(),
                postMember.getMember().getNickname(),
                postMember.getIsOwner(),
                postMember.getCreatedAt(),
                postMember.getUpdatedAt()
        );
    }
}
