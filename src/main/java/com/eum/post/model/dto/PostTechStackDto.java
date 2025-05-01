package com.eum.post.model.dto;

import com.eum.post.model.entity.PostTechStack;

public record PostTechStackDto(
        Long id,
        Long postId,
        Long techStackId,
        String name
) {
    public static PostTechStackDto from(PostTechStack postTechStack) {
        return new PostTechStackDto(
                postTechStack.getId(),
                postTechStack.getPost().getId(),
                postTechStack.getTechStack().getId(),
                postTechStack.getTechStack().getName()
        );
    }
}
