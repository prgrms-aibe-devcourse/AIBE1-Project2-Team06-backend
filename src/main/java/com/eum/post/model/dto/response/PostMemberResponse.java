package com.eum.post.model.dto.response;

import com.eum.post.model.dto.PostMemberDto;

import java.time.LocalDateTime;

public record PostMemberResponse(
        Long id,
        Long postId,
        Long memberId,
        String nickname,
        Boolean isOwner,
        LocalDateTime createdAt
) {
    public static PostMemberResponse from(PostMemberDto dto) {
        return new PostMemberResponse(
                dto.id(),
                dto.postId(),
                dto.memberId(),
                dto.nickname(),
                dto.isOwner(),
                dto.createdAt()
        );
    }
}
