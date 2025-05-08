package com.eum.post.model.dto.request;

import com.eum.member.model.entity.Member;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        //CultureFit cultureFit,
        List<Long> techStackIds,
        List<Long> positionIds
) {

    // userId는 인증된 유저에서 따로 주입
    public Post toEntity(
            Member member
    ) {
        return Post.of(
                member,
                title,
                content,
                recruitType,
                recruitMember,
                progressMethod,
                period,
                deadline,
                linkType,
                link
                //cultureFit
        );
    }
}
