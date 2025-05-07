package com.eum.post.model.dto.request;

import com.eum.post.model.entity.Post;

import java.util.List;

public record PostMemberRequest(
        Long postId,
        List<String> nicknames
){}
