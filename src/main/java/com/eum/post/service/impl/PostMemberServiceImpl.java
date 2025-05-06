package com.eum.post.service.impl;

import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.PostMemberDto;
import com.eum.post.model.dto.request.PostMemberRequest;
import com.eum.post.model.dto.response.PostMemberResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.PostMember;
import com.eum.post.model.repository.PostMemberRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.post.service.PostMemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostMemberServiceImpl implements PostMemberService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostMemberRepository postMemberRepository;

    @Override
    @Transactional
    public List<PostMemberResponse> addMembers(PostMemberRequest request, Long ownerId) {
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + request.postId()));

        // 멤버 닉네임으로 멤버 엔티티 조회
        List<Member> members = new ArrayList<>();
        for (String nickname : request.nicknames()) {
            Member member = memberRepository.findByNickname(nickname)
                    .orElseThrow(() -> new EntityNotFoundException("닉네임으로 멤버를 찾을 수 없습니다: " + nickname));
            members.add(member);
        }

        List<PostMember> postMembers = new ArrayList<>();

        // 모집자(소유자) 추가 (이미 존재하는 경우 제외)
        if (!postMemberRepository.existsByPostIdAndMemberIdAndIsOwnerTrue(post.getId(), ownerId)) {
            Member owner = memberRepository.findById(ownerId)
                    .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. ID: " + ownerId));
            postMembers.add(PostMember.of(post, owner, true));
        }

        // 나머지 멤버 추가 (중복 제외, 소유자 제외)
        for (Member member : members) {
            // 이미 소유자로 추가된 사용자는 건너뜀
            if (member.getId().equals(ownerId)) {
                continue;
            }

            // 이미 추가된 멤버인지 확인
            if (postMemberRepository.findByPostIdAndMemberId(post.getId(), member.getId()).isEmpty()) {
                postMembers.add(PostMember.of(post, member, false));
            }
        }

        List<PostMember> savedPostMembers = postMemberRepository.saveAll(postMembers);

        return savedPostMembers.stream()
                .map(PostMemberDto::from)
                .map(PostMemberResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostMemberResponse> getPostMembers(Long postId) {
        List<PostMember> postMembers = postMemberRepository.findByPostId(postId);

        return postMembers.stream()
                .map(PostMemberDto::from)
                .map(PostMemberResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwner(Long postId, Long memberId) {
        return postMemberRepository.existsByPostIdAndMemberIdAndIsOwnerTrue(postId, memberId);
    }

    @Override
    @Transactional
    public PostMemberResponse addOwner(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. ID: " + memberId));

        // 이미 소유자로 등록되어 있는지 확인
        if (postMemberRepository.existsByPostIdAndMemberIdAndIsOwnerTrue(postId, memberId)) {
            PostMember existingPostMember = postMemberRepository.findByPostIdAndMemberId(postId, memberId)
                    .orElseThrow();

            return PostMemberResponse.from(PostMemberDto.from(existingPostMember));
        }

        PostMember postMember = PostMember.of(post, member, true);
        PostMember savedPostMember = postMemberRepository.save(postMember);

        return PostMemberResponse.from(PostMemberDto.from(savedPostMember));
    }

    @Override
    @Transactional
    public void removeMember(Long postId, Long memberId) {
        PostMember postMember = postMemberRepository.findByPostIdAndMemberId(postId, memberId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글에 멤버가 없습니다."));

        // 소유자는 삭제할 수 없음 (추가 보호 장치)
        if (postMember.getIsOwner()) {
            throw new IllegalArgumentException("게시글 소유자는 삭제할 수 없습니다.");
        }

        postMemberRepository.delete(postMember);
    }
}
