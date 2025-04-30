package com.eum.review.controller;

import com.eum.review.model.dto.request.PeerReviewCreateRequest;
import com.eum.review.model.dto.response.PeerReviewResponse;
import com.eum.review.model.dto.response.UserReviewCommentResponse;
import com.eum.review.model.dto.response.UserReviewScoreResponse;
import com.eum.review.service.PeerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/peer-reviews")
@RequiredArgsConstructor
public class PeerReviewController {
    private final PeerReviewService peerReviewService;

    @PostMapping
    public ResponseEntity<PeerReviewResponse> createReview(
            @RequestBody PeerReviewCreateRequest request,
            @RequestHeader("X-USER-ID") Long currentUserId) {
        PeerReviewResponse response = peerReviewService.createReview(request, currentUserId);
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
