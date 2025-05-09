package com.eum.post.model.dto;

import com.eum.post.model.entity.PostMember;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostMemberDto(
        Long id,
        Long postId,
        Long memberId,
        UUID publicId,
        String nickname,
        Boolean isOwner,
        LocalDateTime createdAt
) {
    public static PostMemberDto from(PostMember postMember) {
        return new PostMemberDto(
                postMember.getId(),
                postMember.getPost().getId(),
                postMember.getMember().getId(),
                postMember.getMember().getPublicId(),
                postMember.getMember().getNickname(),
                postMember.getIsOwner(),
                postMember.getCreatedAt()
        );
    }
}
