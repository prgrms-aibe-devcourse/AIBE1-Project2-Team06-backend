package com.eum.post.service;

import com.eum.post.model.dto.request.PostMemberRequest;
import com.eum.post.model.dto.response.PostMemberResponse;

import java.util.List;

public interface PostMemberService {
    List<PostMemberResponse> addMembers(PostMemberRequest request, Long ownerId);
    List<PostMemberResponse> getPostMembers(Long postId);
    boolean isOwner(Long postId, Long memberId);
    PostMemberResponse addOwner(Long postId, Long memberId);

    void removeMember(Long postId, Long memberId);
}
