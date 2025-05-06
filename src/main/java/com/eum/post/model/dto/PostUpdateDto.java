package com.eum.post.model.dto;

import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.entity.enumerated.*;

import java.time.LocalDate;

public record PostUpdateDto(
        String title,
        String content,
        RecruitType recruitType,
        Integer recruitMember,
        ProgressMethod progressMethod,
        Period period,
        LocalDate deadline,
        LinkType linkType,
        String link,
        CultureFit cultureFit
        ) {
    public static PostUpdateDto from(PostRequest request) {
        return new PostUpdateDto(
                request.title(),
                request.content(),
                request.recruitType(),
                request.recruitMember(),
                request.progressMethod(),
                request.period(),
                request.deadline(),
                request.linkType(),
                request.link(),
                request.cultureFit()
        );
    }
}
