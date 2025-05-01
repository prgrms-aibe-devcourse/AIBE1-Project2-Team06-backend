package com.eum.post.model.dto;

import com.eum.global.model.entity.TechStack;

public record TechStackDto (
        Long id,
        String name
){
    public static TechStackDto from(TechStack techStack) {
        return new TechStackDto(
                techStack.getId(),
                techStack.getName()
        );
    }
}
