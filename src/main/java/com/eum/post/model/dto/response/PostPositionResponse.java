package com.eum.post.model.dto.response;

import com.eum.post.model.dto.PostPositionDto;

public record PostPositionResponse(
        Long id,
        Long postId,  // PostPosition에서 가져오는 postId
        Long positionId,
        String name
) {
    public static PostPositionDto from(PostPositionDto dto) {
        return new PostPositionDto(
                dto.id(),
                dto.postId(),
                dto.positionId(),
                dto.name()
        );
    }
}
