package com.eum.post.model.dto.request;

import java.util.List;

public record PostFilterRequest(
        String keyword,
        String recruitType,
        String progressMethod,
        String cultureFit,
        Boolean isRecruiting,
        Long positionId,
        List<Long> techStackIds
) {}
