package com.eum.post.model.entity;

import com.eum.member.model.entity.Member;
import com.eum.post.model.entity.enumerated.RecruitType;
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
                @UniqueConstraint(columnNames = {"member_id", "post_id"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false, length = 255)
    private String postTitle;

    @Column(columnDefinition = "TEXT")
    private String postLink;

    @Column
    private Double averageScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecruitType recruitType;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public static Portfolio of(
            Member member,
            Long postId,
            String postTitle,
            String postLink,
            Double averageScore,
            RecruitType recruitType
    ) {
        Portfolio portfolio = new Portfolio();
        portfolio.member = member;
        portfolio.postId = postId;
        portfolio.postTitle = postTitle;
        portfolio.postLink = postLink;
        portfolio.averageScore = averageScore;
        portfolio.recruitType = recruitType;
        return portfolio;
    }


}
