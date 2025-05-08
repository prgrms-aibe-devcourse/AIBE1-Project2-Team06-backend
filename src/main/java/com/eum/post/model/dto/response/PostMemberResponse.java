package com.eum.post.model.dto.response;

import com.eum.post.model.dto.PostMemberDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostMemberResponse(
        Long id,
        Long postId,
        UUID publicId,
        String nickname,
        Boolean isOwner,
        LocalDateTime createdAt
) {
    public static PostMemberResponse from(PostMemberDto dto) {
        return new PostMemberResponse(
                dto.id(),
                dto.postId(),
                dto.publicId(),
                dto.nickname(),
                dto.isOwner(),
                dto.createdAt()
        );
    }
}
