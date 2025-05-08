package com.eum.member.service.impl;

import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.global.model.repository.PositionRepository;
import com.eum.global.model.repository.TechStackRepository;
import com.eum.member.model.dto.response.MemberProfileResponseDto;
import com.eum.member.model.entity.Member;
import com.eum.member.model.entity.MemberPosition;
import com.eum.member.model.entity.MemberTechStack;
import com.eum.member.model.repository.MemberRepository;
import com.eum.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PositionRepository positionRepository;
    private final TechStackRepository techStackRepository;

    @Override
    @Transactional
    public void updateProfile(
            UUID memberPublicId,
            String nickname,
            String career,
            String shortDescription,
            Long positionId,
            List<Long> techStackIds
    ) {
        Member member = memberRepository.findByPublicId(memberPublicId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 필드 업데이트
        member.updateProfile(nickname, career, shortDescription);

        // 기존 관계 제거
        member.getMemberPositions().clear();
        member.getMemberTechStacks().clear();

        // Position 추가
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        MemberPosition memberPosition = MemberPosition.of(member, position);
        member.addPosition(memberPosition);

        // TechStacks 추가
        for (Long techStackId : techStackIds) {
            TechStack techStack = techStackRepository.findById(techStackId)
                    .orElseThrow(() -> new RuntimeException("TechStack not found"));
            MemberTechStack mts = MemberTechStack.of(member, techStack);
            member.addTechStack(mts);
        }

        // JPA의 영속성 컨텍스트에 의해 자동 flush됨
    }

    @Override
    @Transactional
    public MemberProfileResponseDto getProfile(UUID memberPublicId) {
        Member member = memberRepository.findByPublicId(memberPublicId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String position = member.getMemberPositions().stream()
                .findFirst()
                .map(mp -> mp.getPosition().getName())
                .orElse(null);

        List<String> techStackNames = member.getMemberTechStacks().stream()
                .map(mts -> mts.getTechStack().getName())
                .toList();

        return new MemberProfileResponseDto(
                member.getNickname(),
                member.getCareer(),
                member.getShortDescription(),
                member.getProfileImageUrl(),
                position,
                techStackNames
        );
    }
}
