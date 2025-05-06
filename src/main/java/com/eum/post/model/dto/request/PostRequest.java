package com.eum.post.model.dto.request;

import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.*;

import java.time.LocalDate;
import java.util.List;

public record PostRequest(
        String title,
        String content,
        RecruitType recruitType,
        Integer recruitMember,
        ProgressMethod progressMethod,
        Period period,
        LocalDate deadline,
        LinkType linkType,
        String link,
        CultureFit cultureFit, // 빌드를 위해 임시 유지
        List<Long> techStackIds,
        List<Long> positionIds
) {

    // userId는 인증된 유저에서 따로 주입
    public Post toEntity(Long userId) {
        // 임시 기본값 설정 (나중에 AI 분석으로 대체)
        CultureFit actualCultureFit = cultureFit != null ? cultureFit : CultureFit.AUTONOMOUS;

        return Post.of(
                userId,
                title,
                content,
                recruitType,
                recruitMember,
                progressMethod,
                period,
                deadline,
                linkType,
                link,
                cultureFit
        );
    }
}
