package com.eum.member.service;

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
}