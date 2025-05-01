package com.eum.post.model.dto.response;

import com.eum.post.model.dto.PositionDto;

public record PositionResponse(
        Long id,
        String name
) {
    public static PositionResponse from(PositionDto dto) {
        return new PositionResponse(
                dto.id(),
                dto.name()
        );
    }
}
