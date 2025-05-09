package com.eum.member.service;

import com.eum.member.model.dto.response.MemberProfileResponseDto;

import java.util.List;
import java.util.UUID;

public interface MemberService {
    void updateProfile(
            UUID memberPublicId,
            String nickname,
            String career,
            String shortDescription,
            Long positionId,
            List<Long> techStackIds
    );

    MemberProfileResponseDto getProfileByPublicId(UUID memberPublicId);

    MemberProfileResponseDto getProfileByNickname(String nickname);

    MemberProfileResponseDto getMyProfile(UUID publicId);
}