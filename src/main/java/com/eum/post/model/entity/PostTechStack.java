package com.eum.post.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Post_TechStack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "techStack_id", nullable = false)
    private TechStack techStack;

    public static PostTechStack of(Post post, TechStack techStack) {
        PostTechStack postTechStack = new PostTechStack();
        postTechStack.post = post;
        postTechStack.techStack = techStack;
        return postTechStack;
    }
}
