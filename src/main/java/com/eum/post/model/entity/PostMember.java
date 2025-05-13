package com.eum.post.model.entity;

import com.eum.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "Post_Member", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "member_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private Boolean isOwner; // 모집자인지 아닌지 체크

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public static PostMember of(Post post, Member member, Boolean isOwner) {
        PostMember postMember = new PostMember();
        postMember.post = post;
        postMember.member = member;
        postMember.isOwner = isOwner;
        return postMember;
    }
}
