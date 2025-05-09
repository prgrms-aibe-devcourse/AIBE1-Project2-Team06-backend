package com.eum.post.model.dto.request;

import java.util.List;

public record PostMemberRequest(
        Long postId,
        List<String> nicknames
){}
