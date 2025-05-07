package com.eum.post.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "portfolio",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false, length = 255)
    private String postTitle;

    @Column(columnDefinition = "TEXT")
    private String postLink;

    @Column
    private Double averageScore;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public static Portfolio of(
            Long userId,
            Long postId,
            String postTitle,
            String postLink,
            Double averageScore
    ) {
        Portfolio portfolio = new Portfolio();
        portfolio.userId = userId;
        portfolio.postId = postId;
        portfolio.postTitle = postTitle;
        portfolio.postLink = postLink;
        portfolio.averageScore = averageScore;
        return portfolio;
    }


}
