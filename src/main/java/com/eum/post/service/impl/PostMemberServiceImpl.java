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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostMemberServiceImpl implements PostMemberService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostMemberRepository postMemberRepository;

    @Override
    public List<PostMemberResponse> updateMembers(Long postId, List<String> nicknames, Long ownerId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));

        // 1. 닉네임 목록으로 멤버 조회
        List<Member> requestedMembers = memberRepository.findAllByNicknameIn(nicknames);

        // 닉네임 유효성 검사
        if (requestedMembers.size() < nicknames.size()) {
            Set<String> foundNicknames = requestedMembers.stream()
                    .map(Member::getNickname)
                    .collect(Collectors.toSet());

            Optional<String> missingNickname = nicknames.stream()
                    .filter(nick -> !foundNicknames.contains(nick))
                    .findFirst();

            if (missingNickname.isPresent()) {
                throw new EntityNotFoundException("닉네임으로 멤버를 찾을 수 없습니다: " + missingNickname.get());
            }
        }

        // 2. 현재 게시글의 모든 멤버 조회
        List<PostMember> existingMembers = postMemberRepository.findAllWithMemberByPostId(postId);

        // 3. 소유자 확인
        PostMember ownerMember = existingMembers.stream()
                .filter(PostMember::getIsOwner)
                .findFirst()
                .orElse(null);

        // 소유자가 없으면 새로 추가
        if (ownerMember == null) {
            Member owner = memberRepository.findById(ownerId)
                    .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. ID: " + ownerId));

            ownerMember = PostMember.of(post, owner, true);
            postMemberRepository.save(ownerMember);
            log.info("게시글 소유자 추가: postId={}, ownerId={}", postId, ownerId);
        }

        // 4. 소유자를 제외한 기존 멤버 삭제
        List<PostMember> membersToRemove = existingMembers.stream()
                .filter(pm -> !pm.getIsOwner())
                .collect(Collectors.toList());

        if (!membersToRemove.isEmpty()) {
            postMemberRepository.deleteAll(membersToRemove);
            log.info("게시글 멤버 삭제: postId={}, count={}", postId, membersToRemove.size());
        }

        // 5. 새 멤버 목록 생성 (소유자 제외)
        Set<Long> memberIdsToAdd = requestedMembers.stream()
                .map(Member::getId)
                .filter(id -> !id.equals(ownerId)) // 소유자 제외
                .collect(Collectors.toSet());

        List<PostMember> newMembers = new ArrayList<>();
        for (Member member : requestedMembers) {
            if (memberIdsToAdd.contains(member.getId())) {
                newMembers.add(PostMember.of(post, member, false));
            }
        }

        List<PostMember> savedMembers = Collections.emptyList();
        if (!newMembers.isEmpty()) {
            savedMembers = postMemberRepository.saveAll(newMembers);
            log.info("게시글 멤버 추가: postId={}, count={}", postId, savedMembers.size());
        }

        // 6. 결과 조회 및 반환
        List<PostMember> resultMembers = postMemberRepository.findAllWithMemberByPostId(postId);

        return resultMembers.stream()
                .map(member -> PostMemberResponse.from(PostMemberDto.from(member)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostMemberResponse> getPostMembers(Long postId) {
        return postMemberRepository.findPostMembersWithMemberByPostId(postId).stream()
                .map(member -> PostMemberResponse.from(PostMemberDto.from(member)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwner(Long postId, Long memberId) {
        return postMemberRepository.existsByPostIdAndMemberIdAndIsOwnerTrue(postId, memberId);
    }

}
