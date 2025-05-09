package com.eum.member.model.dto.response;

import java.util.List;

public record MemberProfileResponseDto(
        String nickname,
        String career,
        String shortDescription,
        String profileImageUrl,
        String position,
        List<String> techStacks
) {}
