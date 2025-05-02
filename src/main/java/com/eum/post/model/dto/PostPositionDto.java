package com.eum.post.model.dto;

import com.eum.post.model.entity.PostPosition;

public record PostPositionDto(
        Long id,
        Long postId,
        Long positionId,
        String name
) {
    public static PostPositionDto from(PostPosition postPosition) {
        return new PostPositionDto(
                postPosition.getId(),
                postPosition.getPost().getId(),
                postPosition.getPosition().getId(),
                postPosition.getPosition().getName()
        );
    }
}
