package com.eum.post.controller;

import com.eum.post.model.dto.request.PostMemberRequest;
import com.eum.post.model.dto.response.PostMemberResponse;
import com.eum.post.service.PostMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostMemberController {

    private final PostMemberService postMemberService;

    /**
     * 게시글에 멤버 추가 API
     */
    @PostMapping("/{postId}/members")
    public ResponseEntity<List<PostMemberResponse>> updateMembers(
            @PathVariable Long postId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody PostMemberRequest request) {

        // 본인이 게시글 모집자인지 확인 (권한 검사)
        if (!postMemberService.isOwner(postId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<PostMemberResponse> responses = postMemberService.updateMembers(postId, request.nicknames(), userId);
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
