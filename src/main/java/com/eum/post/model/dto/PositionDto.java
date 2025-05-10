package com.eum.post.model.dto;

import com.eum.global.model.entity.Position;

public record PositionDto(
        Long id,
        String name
) {
    public static PositionDto from(Position position) {
        return new PositionDto(
                position.getId(),
                position.getName()
        );
    }
}
