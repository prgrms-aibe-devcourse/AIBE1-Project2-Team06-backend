package com.eum.post.controller;

import com.eum.post.model.dto.request.PostMemberRequest;
import com.eum.post.model.dto.response.PostMemberResponse;
import com.eum.post.service.PostMemberService;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<List<PostMemberResponse>> addMembers(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody PostMemberRequest request) {

        Long postId = request.postId();

        // 본인이 게시글 모집자인지 확인 (권한 검사)
        if (!postMemberService.isOwner(postId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            List<PostMemberResponse> responses = postMemberService.addMembers(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * 게시글 멤버 목록 조회 API
     */
    @GetMapping("/{postId}/members")
    public ResponseEntity<List<PostMemberResponse>> getMembers(@PathVariable Long postId) {
        List<PostMemberResponse> responses = postMemberService.getPostMembers(postId);
        return ResponseEntity.ok(responses);
    }

    /**
     * 게시글 멤버 삭제 API
     */
    @DeleteMapping("/{postId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long postId,
            @PathVariable Long memberId,
            @RequestHeader("X-USER-ID") Long userId) {

        // 본인이 게시글 모집자인지 확인 (권한 검사)
        if (!postMemberService.isOwner(postId, userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            postMemberService.removeMember(postId, memberId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
