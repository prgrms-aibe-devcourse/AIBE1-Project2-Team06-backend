package com.eum.post.model.dto;

import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;

import java.util.List;

public record PostFilterDto(
        String keyword,
        String recruitType,
        String progressMethod,
        String cultureFit,
        Long positionId,
        List<Long> techStackIds,
        Integer page,
        Integer size,
        String sort
) {
    public static PostFilterDto of(
            String keyword,
            String recruitType,
            String progressMethod,
            String cultureFit,
            Long positionId,
            List<Long> techStackIds,
            Integer page,
            Integer size,
            String sort
    ) {
        return new PostFilterDto(
                keyword,
                recruitType,
                progressMethod,
                cultureFit,
                positionId,
                techStackIds,
                page != null ? page : 0,
                size != null ? size : 10,
                sort != null ? sort : "createdAt,desc"
        );
    }

    // Enum 변환 메서드들 추가
    public RecruitType getRecruitTypeEnum() {
        if (recruitType == null || recruitType.isEmpty()) {
            return null;
        }
        try {
            return RecruitType.valueOf(recruitType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public ProgressMethod getProgressMethodEnum() {
        if (progressMethod == null || progressMethod.isEmpty()) {
            return null;
        }
        try {
            return ProgressMethod.valueOf(progressMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public CultureFit getCultureFitEnum() {
        if (cultureFit == null || cultureFit.isEmpty()) {
            return null;
        }
        try {
            return CultureFit.valueOf(cultureFit.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // 기본값이 있는 필드들을 위한 접근자 메서드 (컨트롤러에서 사용)
    public int getPageValue() {
        return page != null ? page : 0;
    }

    public int getSizeValue() {
        return size != null ? size : 5;
    }

    public String getSortValue() {
        return sort != null ? sort : "createdAt,desc";
    }
}
