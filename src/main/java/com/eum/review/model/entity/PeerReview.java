package com.eum.review.model.entity;

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
            @UniqueConstraint(columnNames = {"reviewer_user_id", "reviewee_user_id", "project_id"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reviewer_user_id", nullable = false)
    private Long reviewerUserId;

    @Column(name = "reviewee_user_id", nullable = false)
    private Long revieweeUserId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "collaboration_score", nullable = false)
    private Integer collaborationScore;

    @Column(name = "technical_score", nullable = false)
    private Integer technicalScore;

    @Column(name = "work_again_score", nullable = false)
    private Integer workAgainScore;

    @Column(name = "average_score", nullable = false)
    private Double averageScore;

    @Column(name = "review_comment")
    private String reviewComment;

    @CreationTimestamp
    @Column(name = "review_date", nullable = false, updatable = false)
    private LocalDateTime reviewDate;


    public static PeerReview of(
            Long reviewerUserId,
            Long revieweeUserId,
            Long projectId,
            Integer collaborationScore,
            Integer technicalScore,
            Integer workAgainScore,
            String reviewComment
    ) {
        PeerReview peerReview = new PeerReview();
        peerReview.reviewerUserId = reviewerUserId;
        peerReview.revieweeUserId = revieweeUserId;
        peerReview.projectId = projectId;
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
