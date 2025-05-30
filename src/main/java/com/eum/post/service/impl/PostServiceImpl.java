package com.eum.post.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.global.model.repository.PositionRepository;
import com.eum.global.model.repository.TechStackRepository;
import com.eum.member.model.entity.Member;
import com.eum.member.model.repository.MemberRepository;
import com.eum.post.model.dto.PositionDto;
import com.eum.post.model.dto.PostDto;
import com.eum.post.model.dto.TechStackDto;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostUpdateResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.PostMember;
import com.eum.post.model.entity.PostPosition;
import com.eum.post.model.entity.PostTechStack;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.*;
import com.eum.post.service.PortfolioService;
import com.eum.post.service.PostService;
import com.eum.post.validation.ValidatePostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostTechStackRepository postTechStackRepository;
    private final PostPositionRepository postPositionRepository;
    private final PositionRepository positionRepository;
    private final TechStackRepository techStackRepository;
    private final PostMemberRepository postMemberRepository;
    private final MemberRepository memberRepository;

    private final PortfolioService portfolioService;

    @Override
    @Transactional
    public PostDto create(
            PostRequest postRequest,
            UUID publicId
    ) {
        ValidatePostRequest.validatePostRequest(postRequest);

        // Member 조회
        Member owner = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND,"멤버를 찾을 수 없습니다. ID: " + publicId));

        // Post 엔티티 생성 및 저장
        Post post = postRequest.toEntity(owner);
        Post savedPost = postRepository.save(post);

        // 3. 모집자(PostMember)로 등록
        PostMember postMember = PostMember.of(savedPost, owner, true);
        postMemberRepository.save(postMember);

        // techStack 연결
        List<TechStackDto> techStackDtos = saveTechStacks(savedPost, postRequest.techStackIds());

        // position 연결
        List<PositionDto> positionDtos = savePositions(savedPost, postRequest.positionIds());

        return PostDto.from(post, techStackDtos, positionDtos);
    }

    // 기술 스택 저장 메소드
    private List<TechStackDto> saveTechStacks(Post post, List<Long> techStackIds) {
        List<TechStackDto> result = new ArrayList<>();

        for (Long techStackId : techStackIds) {
            // 1. 기술 스택 엔티티 조회
            TechStack techStack = techStackRepository.findById(techStackId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND,
                            "기술 스택을 찾을 수 없습니다. ID: " + techStackId));

            // 2. 게시글-기술스택 연결 엔티티 생성 및 저장
            PostTechStack postTechStack = PostTechStack.of(post, techStack);
            postTechStackRepository.save(postTechStack);

            // 3. DTO 변환 및 결과 리스트에 추가
            result.add(TechStackDto.from(techStack));
        }

        return result;
    }

    // 포지션 저장 메소드
    private List<PositionDto> savePositions(Post post, List<Long> positionIds) {
        List<PositionDto> result = new ArrayList<>();

        for (Long positionId : positionIds) {
            // 1. 포지션 엔티티 조회
            Position position = positionRepository.findById(positionId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND,
                            "포지션을 찾을 수 없습니다. ID: " + positionId));

            // 2. 게시글-포지션 연결 엔티티 생성 및 저장
            PostPosition postPosition = PostPosition.of(post, position);
            postPositionRepository.save(postPosition);

            // 3. DTO 변환 및 결과 리스트에 추가
            result.add(PositionDto.from(position));
        }
        return result;
    }

    // 게시글에 연결된 기술 스택 조회
    private List<TechStackDto> findTechStacksByPostId(Long postId) {
        List<PostTechStack> postTechStacks = postTechStackRepository.findByPostId(postId);
        return postTechStacks.stream()
                .map(pts -> TechStackDto.from(pts.getTechStack()))
                .collect(Collectors.toList());
    }

    // 게시글에 연결된 포지션 조회
    private List<PositionDto> findPositionsByPostId(Long postId) {
        List<PostPosition> postPositions = postPositionRepository.findByPostId(postId);
        return postPositions.stream()
                .map(pp -> PositionDto.from(pp.getPosition()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto findByPostId(Long postId) {
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 관련 기술 스택 조회
        List<TechStackDto> techStackDtos = findTechStacksByPostId(post.getId());

        // 관련 포지션 조회
        List<PositionDto> positionDtos = findPositionsByPostId(post.getId());

        return PostDto.from(post, techStackDtos, positionDtos);
    }

    @Override
    @Transactional
    public PostDto update(
            Long postId,
            PostRequest postRequest,
            UUID publicId
    ) {
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 검증
        if (!post.getMemberPublicId().equals(publicId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED,"해당 게시글의 수정 권한이 없습니다.");
        }

        // 마감된 게시글 수정 제한
        if (post.getStatus() == Status.CLOSED) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED,"마감된 게시글은 수정할 수 없습니다.");
        }

        // 요청 유효성 검증
        ValidatePostRequest.validatePostRequest(postRequest);

        // PostRequest를 PostUpdateDto로 변환하고 엔티티 업데이트
        PostUpdateResponse updateDto = PostUpdateResponse.from(postRequest);
        post.updatePost(updateDto);

        // 기존 연결된 기술 스택 및 포지션 삭제
        List<PostTechStack> techStacks = postTechStackRepository.findByPostId(postId);
        List<PostPosition> positions = postPositionRepository.findByPostId(postId);
        postTechStackRepository.deleteAll(techStacks);
        postPositionRepository.deleteAll(positions);

        // 새로운 기술 스택 및 포지션 연결
        List<TechStackDto> techStackDtos = saveTechStacks(post, postRequest.techStackIds());
        List<PositionDto> positionDtos = savePositions(post, postRequest.positionIds());

        return PostDto.from(post, techStackDtos, positionDtos);
    }

    @Override
    @Transactional
    public void deletePost(
            Long postId,
            UUID publicId
    ) {
        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 확인 (권한 검증)
        if (post.getMemberPublicId().equals(publicId)) {
            // 게시글과 연결된 기술 스택 제거
            List<PostTechStack> postTechStacks = postTechStackRepository.findByPostId(postId);
            postTechStackRepository.deleteAll(postTechStacks);

            // 게시글과 연결된 포지션 제거
            List<PostPosition> postPositions = postPositionRepository.findByPostId(postId);
            postPositionRepository.deleteAll(postPositions);

            postRepository.delete(post);
        } else {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED,"해당 게시글의 삭제 권한이 없습니다.");
        }
    }

    //merge 된 부분
    @Override
    @Transactional
    public PostDto completePost(Long postId, UUID publicId, String githubLink) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMemberPublicId().equals(publicId)){
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED,"프로젝트 작성자만 완료 처리할 수 있습니다.");
        }

        post.updateStatus(Status.COMPLETED);

        Member member = memberRepository.findByPublicId(publicId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        portfolioService.createPortfolio(member, postId, githubLink);

//        List<TechStackDto> techStackDtos = findTechStacksByPostId(postId);
//        List<PositionDto> positionDtos = findPositionsByPostId(postId);
//
//        return PostDto.from(post, techStackDtos, positionDtos);

        // 최적화된 방식으로 연관 데이터 한 번에 조회
        List<PostTechStack> techStacks = postTechStackRepository.findByPostIdInWithTechStack(List.of(postId));
        List<PostPosition> positions = postPositionRepository.findByPostIdInWithPosition(List.of(postId));

        // DTO 변환
        List<TechStackDto> techStackDtos = techStacks.stream()
                .map(pts -> TechStackDto.from(pts.getTechStack()))
                .collect(Collectors.toList());

        List<PositionDto> positionDtos = positions.stream()
                .map(pp -> PositionDto.from(pp.getPosition()))
                .collect(Collectors.toList());

        // 최종 DTO 반환
        return PostDto.from(post, techStackDtos, positionDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> findPostsWithFilters(
            String keyword,
            RecruitType recruitType,
            ProgressMethod progressMethod,
            CultureFit cultureFit,
            //Status status,
            Boolean isRecruiting,  // 추가된 매개변수
            Long positionId,
            List<Long> techStackIds,
            Pageable pageable) {

        // 명세 조합
        Specification<Post> spec = Specification.where(null);

        // 키워드 검색 조건 추가
        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and(PostSpecification.hasKeyword(keyword));
        }

        // 모집 방식에 대한 필터링
        if (recruitType != null) {
            spec = spec.and(PostSpecification.hasRecruitType(recruitType));
        }

        // 진행 방식에 대한 필터링
        if (progressMethod != null) {
            spec = spec.and(PostSpecification.hasProgressMethod(progressMethod));
        }

        // 컬처핏에 대한 필터링
        if (cultureFit != null) {
            spec = spec.and(PostSpecification.hasCultureFit(cultureFit));
        }

        // 모집 중인 글만 보기 필터링
        if (isRecruiting != null && isRecruiting) {
            spec = spec.and(PostSpecification.isRecruiting());
        }

        // 포지션에 대한 필터링
        if (positionId != null) {
            spec = spec.and(PostSpecification.hasPosition(positionId));
        }

        // 기술 스택에 대한 필터링
        if (techStackIds != null && !techStackIds.isEmpty()) {
            spec = spec.and(PostSpecification.hasTechStacks(techStackIds));
        }

        // 필터링된 결과 조회
        Page<Post> posts = postRepository.findAll(spec, pageable);

//        // 응답 변환
//        return posts.map(post -> {
//            // 각 게시글에 연결된 기술 스택과 포지션 조회
//            List<TechStackDto> techStackDtos = findTechStacksByPostId(post.getId());
//            List<PositionDto> positionDtos = findPositionsByPostId(post.getId());
//
//            return PostDto.from(post, techStackDtos, positionDtos);
//        });

        // 게시글 ID 목록 추출
        List<Long> postIds = posts.getContent().stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // 한 번에 모든 TechStack 및 Position 정보 조회
        List<PostTechStack> allTechStacks = postIds.isEmpty() ?
                List.of() :
                postTechStackRepository.findByPostIdInWithTechStack(postIds);

        List<PostPosition> allPositions = postIds.isEmpty() ?
                List.of() :
                postPositionRepository.findByPostIdInWithPosition(postIds);

        // 게시글 ID별로 TechStack 및 Position 그룹화
        Map<Long, List<TechStackDto>> techStacksByPostId = allTechStacks.stream()
                .collect(Collectors.groupingBy(
                        pts -> pts.getPost().getId(),
                        Collectors.mapping(
                                pts -> TechStackDto.from(pts.getTechStack()),
                                Collectors.toList()
                        )
                ));

        Map<Long, List<PositionDto>> positionsByPostId = allPositions.stream()
                .collect(Collectors.groupingBy(
                        pp -> pp.getPost().getId(),
                        Collectors.mapping(
                                pp -> PositionDto.from(pp.getPosition()),
                                Collectors.toList()
                        )
                ));

        // DTO 변환
        return posts.map(post -> {
            Long postId = post.getId();
            List<TechStackDto> techStackDtos = techStacksByPostId.getOrDefault(postId, new ArrayList<>());
            List<PositionDto> positionDtos = positionsByPostId.getOrDefault(postId, new ArrayList<>());

            return PostDto.from(post, techStackDtos, positionDtos);
        });
    }
}
