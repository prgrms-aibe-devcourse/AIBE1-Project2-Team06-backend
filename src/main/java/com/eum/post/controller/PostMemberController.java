package com.eum.post.controller;

import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.request.PostMemberRequest;
import com.eum.post.model.dto.response.PostMemberResponse;
import com.eum.post.service.PostMemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostMemberController {

    private final PostMemberService postMemberService;
    private final MemberRepository memberRepository;

    /**
     * 게시글에 멤버 추가 API
     */
    @PostMapping("/{postId}/members")
    public ResponseEntity<List<PostMemberResponse>> updateMembers(
        @PathVariable Long postId,
        @RequestBody PostMemberRequest request,
        HttpServletRequest httpRequest
    )
    {
        UUID publicId = (UUID) httpRequest.getAttribute("publicId");

        // publicId로 Member 조회하여 내부 ID 얻기
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));

        // 본인이 게시글 모집자인지 확인 (권한 검사)
        if (!postMemberService.isOwner(postId, member.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 내부 ID 사용하여 서비스 메서드 호출
        List<PostMemberResponse> responses = postMemberService.updateMembers(
                postId,
                request.nicknames(),
                member.getId()  // 내부 ID 전달
        );

        return ResponseEntity.ok(responses);
    }

    /**
     * 게시글 멤버 목록 조회 API
     */
    @GetMapping("/{postId}/members")
    public ResponseEntity<List<PostMemberResponse>> getMembers(@PathVariable Long postId) {
        List<PostMemberResponse> responses = postMemberService.getPostMembers(postId);
        return ResponseEntity.ok(responses);
    }
}
