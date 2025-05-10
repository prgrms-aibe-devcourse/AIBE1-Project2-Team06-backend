package com.eum.review.controller;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.UserReviewCommentResponse;
import com.eum.review.model.dto.response.UserReviewScoreResponse;
import com.eum.review.service.PeerReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/peer-reviews")
@RequiredArgsConstructor
public class PeerReviewController {
    private final PeerReviewService peerReviewService;
    private final MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<PeerReviewResponse> createReview(
            @RequestBody PeerReviewCreateRequest request,
            HttpServletRequest httpServletRequest) {

        UUID publicId = (UUID) httpServletRequest.getAttribute("publicId");

        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        PeerReviewResponse response = peerReviewService.createReview(request, member.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/{userId}/score")
    public ResponseEntity<UserReviewScoreResponse> getUserReviewScore(@PathVariable Long userId) {
        UserReviewScoreResponse response = peerReviewService.calculateUserReviewScore(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/comments")
    public ResponseEntity<List<UserReviewCommentResponse>> getUserReviewComments(@PathVariable Long userId) {
        List<UserReviewCommentResponse> comments = peerReviewService.getUserReviewComments(userId);
        return ResponseEntity.ok(comments);
    }
}
