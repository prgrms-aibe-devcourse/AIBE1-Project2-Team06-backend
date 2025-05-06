package com.eum.member.model.dto.response;

public record LoginResponseDto(
        Long memberId,
        String authId,
        String token
) {}
