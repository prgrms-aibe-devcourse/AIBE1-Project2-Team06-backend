package com.eum.post.service.impl;

import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.request.PostMemberRequest;
import com.eum.post.model.dto.response.PostMemberResponse;
import com.eum.post.model.repository.PostMemberRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.post.service.PostMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMemberServiceImpl implements PostMemberService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostMemberRepository postMemberRepository;

    @Override
    public List<PostMemberResponse> addMembers(PostMemberRequest request, Long ownerId) {
        return List.of();
    }

    @Override
    public List<PostMemberResponse> getPostMembers(Long postId) {
        return List.of();
    }

    @Override
    public boolean isOwner(Long postId, Long memberId) {
        return false;
    }

    @Override
    public PostMemberResponse addOwner(Long postId, Long memberId) {
        return null;
    }
}
