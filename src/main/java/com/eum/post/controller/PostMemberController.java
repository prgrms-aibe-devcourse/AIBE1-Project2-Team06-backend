package com.eum.post.controller;

import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.PostMemberDto;
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
import java.util.stream.Collectors;

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

        return ResponseEntity.ok(
                postMemberService.updateMembers(postId, request.nicknames(), publicId)
                        .stream()
                        .map(PostMemberResponse::from)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 게시글 멤버 목록 조회 API
     */
    @GetMapping("/{postId}/members")
    public ResponseEntity<List<PostMemberResponse>> getMembers(@PathVariable Long postId) {
        return ResponseEntity.ok(
                postMemberService.getPostMembers(postId)
                        .stream()
                        .map(PostMemberResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
