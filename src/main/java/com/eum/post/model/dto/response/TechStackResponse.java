package com.eum.post.model.dto.response;

import com.eum.post.model.dto.TechStackDto;

public record TechStackResponse(
        Long id,
        String name
) {
    public static TechStackResponse from(TechStackDto dto) {
        return new TechStackResponse(
                dto.id(),
                dto.name()
        );
    }
}
