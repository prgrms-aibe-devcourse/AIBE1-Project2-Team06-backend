package com.eum.post.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "Post_Position")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    public static PostPosition of(Post post, Position position) {
        PostPosition postPosition = new PostPosition();
        postPosition.post = post;
        postPosition.position = position;
        return postPosition;
    }

}
