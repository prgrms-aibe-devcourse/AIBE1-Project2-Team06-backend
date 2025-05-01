package com.eum.post.model.dto;

import com.eum.global.model.entity.Position;
import com.eum.post.model.dto.response.PositionResponse;
import com.eum.post.model.dto.response.TechStackResponse;

import java.util.List;

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
