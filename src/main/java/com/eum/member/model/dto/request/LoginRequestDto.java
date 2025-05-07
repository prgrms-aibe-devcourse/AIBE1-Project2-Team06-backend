package com.eum.member.model.dto.request;

public record LoginRequestDto(
        String code,
        String provider
) {}
