package com.eum.review.model.entity;

import com.eum.post.model.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "peer_review",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"reviewer_user_id", "reviewee_user_id", "post_id"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long reviewerMemberId;

    @Column(nullable = false)
    private Long revieweeMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private Integer collaborationScore;

    @Column(nullable = false)
    private Integer technicalScore;

    @Column(nullable = false)
    private Integer workAgainScore;

    @Column(nullable = false)
    private Double averageScore;

    private String reviewComment;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime reviewDate;


    public static PeerReview of(
            Long reviewerMemberId,
            Long revieweeMemberId,
            Post post,
            Integer collaborationScore,
            Integer technicalScore,
            Integer workAgainScore,
            String reviewComment
    ) {
        PeerReview peerReview = new PeerReview();
        peerReview.reviewerMemberId = reviewerMemberId;
        peerReview.revieweeMemberId = revieweeMemberId;
        peerReview.post = post;
        peerReview.collaborationScore = collaborationScore;
        peerReview.technicalScore = technicalScore;
        peerReview.workAgainScore = workAgainScore;
        peerReview.reviewComment = reviewComment;

        peerReview.calculateAverageScore();
        return peerReview;
    }

    private void calculateAverageScore() {
        this.averageScore = (this.collaborationScore + this.technicalScore + this.workAgainScore) / 3.0;
    }

}
