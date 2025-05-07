package com.eum.post.model.dto.response;

import com.eum.post.model.dto.PostTechStackDto;

public record PostTechStackResponse(
        Long id,
        Long postId,
        Long teckStackId,
        String name
) {
    public static PostTechStackResponse from(PostTechStackDto dto) {
        return new PostTechStackResponse(
                dto.id(),
                dto.postId(),
                dto.techStackId(),
                dto.name()
        );
    }
}
