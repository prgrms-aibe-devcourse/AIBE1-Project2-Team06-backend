package com.eum.member.controller;

import com.eum.member.model.dto.request.UpdateProfileRequest;
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
    private final MemberServiceImpl memberService;

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            HttpServletRequest httpServletRequest,
            @RequestBody UpdateProfileRequest request
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
}
