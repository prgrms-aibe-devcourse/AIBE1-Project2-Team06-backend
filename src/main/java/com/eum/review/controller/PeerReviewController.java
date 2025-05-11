package com.eum.review.controller;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.MemberReviewCommentResponse;
import com.eum.review.model.dto.response.MemberReviewScoreResponse;
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

        UUID reviewerpublicId = (UUID) httpServletRequest.getAttribute("publicId");

        Member reviewer = memberRepository.findByPublicId(reviewerpublicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Member reviewee = memberRepository.findByPublicId(request.revieweePublicId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, "리뷰 대상자를 찾을 수 없습니다."));

        PeerReviewResponse response = peerReviewService.createReview(
                request,
                reviewer.getId(),
                reviewee.getId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/member/{publicId}/score")
    public ResponseEntity<MemberReviewScoreResponse> getUserReviewScore(@PathVariable UUID publicId) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        MemberReviewScoreResponse response = peerReviewService.calculateUserReviewScore(member.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{publicId}/comments")
    public ResponseEntity<List<MemberReviewCommentResponse>> getUserReviewComments(@PathVariable UUID publicId) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<MemberReviewCommentResponse> comments = peerReviewService.getUserReviewComments(member.getId());
        return ResponseEntity.ok(comments);
    }
}
