package com.eum.post.service.impl;

import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.PostMemberDto;
import com.eum.post.model.dto.response.PostMemberResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.PostMember;
import com.eum.post.model.entity.enumerated.Status;
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
    @Transactional
    public List<PostMemberDto> updateMembers(
            Long postId,
            List<String> nicknames,
            Long ownerId
    ) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));

        // 닉네임 목록으로 멤버 조회
        List<Member> requestedMembers = memberRepository.findAllByNicknameIn(nicknames);
        // 닉네임 유효성 검사 (추가적 검증)
        validateMembers(nicknames, requestedMembers);
        // 현재 게시글의 모든 멤버 조회
        List<PostMember> existingMembers = postMemberRepository.findAllWithMemberByPostId(postId);
        // 모집자 확인
        ensureOwnerExists(postId, ownerId, existingMembers, post);
        // 모집자를 제외한 기존 멤버 삭제
        removeExistingNonOwnerMembers(postId, existingMembers);
        // 새 멤버 목록 생성 (모집자 제외)
        addNewMembers(postId, ownerId, requestedMembers, post);

        // 결과 조회 및 반환
        List<PostMember> resultMembers = postMemberRepository.findAllWithMemberByPostId(postId);
        return resultMembers.stream()
                .map(PostMemberDto::from)  // 직접 PostMemberDto로 변환
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostMemberDto> getPostMembers(Long postId) {
        return postMemberRepository.findAllWithMemberByPostId(postId).stream()
                .map(PostMemberDto::from)  // 직접 PostMemberDto로 변환
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isOwner(Long postId,
                           Long memberId
    ) {
        return postMemberRepository.existsByPostIdAndMemberIdAndIsOwnerTrue(postId, memberId);
    }

    private void addNewMembers(Long postId, Long ownerId, List<Member> requestedMembers, Post post) {
        Set<Long> membersToAdd = requestedMembers.stream()
                .map(Member::getId)
                .filter(publicId -> !publicId.equals(ownerId))  // 소유자 제외
                .collect(Collectors.toSet());

        List<PostMember> newMembers = new ArrayList<>();
        for (Member member : requestedMembers) {
            if (membersToAdd.contains(member.getId())) {
                newMembers.add(PostMember.of(post, member, false));
            }
        }

        if (!newMembers.isEmpty()) {
            List<PostMember> savedMembers = postMemberRepository.saveAll(newMembers);
            log.info("게시글 멤버 추가: postId={}, count={}", postId, savedMembers.size());

            // 멤버가 추가되었을 때 게시글 상태를 ONGOING으로 변경
            if (post.getStatus() == Status.RECRUITING || post.getStatus() == Status.CLOSED) {
                post.updateStatus(Status.ONGOING);
                postRepository.save(post);
                log.info("게시글 상태가 ONGOING으로 변경됨: postId={}", postId);
            }
        }
    }

    private void removeExistingNonOwnerMembers(Long postId, List<PostMember> existingMembers) {
        List<PostMember> membersToRemove = existingMembers.stream()
                .filter(member -> !member.getIsOwner())
                .collect(Collectors.toList());

        if (!membersToRemove.isEmpty()) {
            postMemberRepository.deleteAll(membersToRemove);
            log.info("게시글 멤버 삭제: postId={}, count={}", postId, membersToRemove.size());
        }
    }

    private void ensureOwnerExists(Long postId, Long ownerId, List<PostMember> existingMembers, Post post) {
        PostMember ownerMember = existingMembers.stream()
                .filter(PostMember::getIsOwner)
                .findFirst()
                .orElse(null);

        // 모집자가 없으면 새로 추가 (추가적인 검증)
        if (ownerMember == null) {
            Member owner = memberRepository.findById(ownerId)
                    .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다. ID: " + ownerId));

            ownerMember = PostMember.of(post, owner, true);
            postMemberRepository.save(ownerMember);
            log.info("게시글 소유자 추가: postId={}, ownerId={}", postId, ownerId);
        }
    }

    private static void validateMembers(List<String> nicknames, List<Member> requestedMembers) {
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
    }
}
