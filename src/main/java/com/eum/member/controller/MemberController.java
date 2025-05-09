package com.eum.member.controller;

import com.eum.member.model.dto.request.UpdateProfileRequestDto;
import com.eum.member.model.dto.response.MemberProfileResponseDto;
import com.eum.member.service.MemberService;
import com.eum.member.service.impl.MemberServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            HttpServletRequest httpServletRequest,
            @RequestBody UpdateProfileRequestDto request
    ) {
        UUID memberPublicId = (UUID) httpServletRequest.getAttribute("publicId");

        memberService.updateProfile(
                memberPublicId,
                request.nickname(),
                request.career(),
                request.shortDescription(),
                request.positionId(),
                request.techStackIds()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile/public-id/{publicId}")
    public ResponseEntity<MemberProfileResponseDto> getProfileByPublicId(@PathVariable UUID publicId) {
        MemberProfileResponseDto profile = memberService.getProfileByPublicId(publicId);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<MemberProfileResponseDto> getProfileByPublicId(@PathVariable String nickname) {
        MemberProfileResponseDto profile = memberService.getProfileByNickname(nickname);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profile/me")
    public ResponseEntity<MemberProfileResponseDto> getMyProfile(HttpServletRequest request) {
        UUID publicId = (UUID) request.getAttribute("publicId");
        MemberProfileResponseDto profile = memberService.getMyProfile(publicId);
        return ResponseEntity.ok(profile);
    }
}
