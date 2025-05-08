package com.eum.member.model.dto.request;

import java.util.List;

public record UpdateProfileRequestDto(
        String nickname,
        String career,
        String shortDescription,
        Long positionId,
        List<Long> techStackIds
) {}
