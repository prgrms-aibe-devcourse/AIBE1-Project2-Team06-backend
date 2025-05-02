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
                @UniqueConstraint(columnNames = {"user_id", "project_id"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false, length = 255)
    private String projectTitle;

    @Column(columnDefinition = "TEXT")
    private String projectLink;

    @Column
    private Double averageScore;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public static Portfolio of(
            Long userId,
            Long projectId,
            String projectTitle,
            String projectLink,
            Double averageScore
    ) {
        Portfolio portfolio = new Portfolio();
        portfolio.userId = userId;
        portfolio.projectId = projectId;
        portfolio.projectTitle = projectTitle;
        portfolio.projectLink = projectLink;
        portfolio.averageScore = averageScore;
        return portfolio;
    }


}
