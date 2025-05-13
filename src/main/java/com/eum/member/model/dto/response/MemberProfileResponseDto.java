package com.eum.member.model.dto.response;

import java.util.List;
import java.util.UUID;

public record MemberProfileResponseDto(
		UUID publicId,
        String nickname,
        String career,
        String shortDescription,
        String profileImageUrl,
        String position,
        List<String> techStacks
) {}
