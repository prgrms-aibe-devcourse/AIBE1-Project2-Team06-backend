package com.eum.post.service.impl;

import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.global.model.repository.PositionRepository;
import com.eum.global.model.repository.TechStackRepository;
import com.eum.post.model.dto.PositionDto;
import com.eum.post.model.dto.PostDto;
import com.eum.post.model.dto.TechStackDto;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.dto.response.PostUpdateResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.PostPosition;
import com.eum.post.model.entity.PostTechStack;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.entity.enumerated.ProgressMethod;
import com.eum.post.model.entity.enumerated.RecruitType;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.PostPositionRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.post.model.repository.PostSpecification;
import com.eum.post.model.repository.PostTechStackRepository;
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

    private final PortfolioService portfolioService;

    @Override
    public PostResponse create(PostRequest postRequest, Long userId) {
        ValidatePostRequest.validatePostRequest(postRequest);

        try{
            // Post 엔티티 생성 및 저장
            Post post = postRequest.toEntity(userId);
            Post savedPost = postRepository.save(post);

            // techStack 연결
            List<TechStackDto> techStackDtos = saveTechStacks(savedPost, postRequest.techStackIds());

            // position 연결
            List<PositionDto> positionDtos = savePositions(savedPost, postRequest.positionIds());

            // 4. DTO 변환 및 반환
            PostDto postDto = PostDto.from(savedPost, techStackDtos, positionDtos);
            return PostResponse.from(postDto);
        } catch (Exception e) {
            // 예외 발생 시 트랜잭션이 롤백됨
            log.error("게시글 생성 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
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
    public PostResponse findByPostId(Long postId) {
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 관련 기술 스택 조회
        List<TechStackDto> techStackDtos = findTechStacksByPostId(post.getId());

        // 관련 포지션 조회
        List<PositionDto> positionDtos = findPositionsByPostId(post.getId());

        // DTO 변환 및 반환
        PostDto postDto = PostDto.from(post, techStackDtos, positionDtos);
        return PostResponse.from(postDto);
    }

    @Override
    @Transactional
    public PostResponse update(Long postId, PostRequest postRequest, Long userId) {
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 검증
        if (!post.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED);
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

        // DTO 변환 및 반환
        PostDto postDto = PostDto.from(post, techStackDtos, positionDtos);
        return PostResponse.from(postDto);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        // 작성자 확인 (권한 검증)
        if (post.getUserId().equals(userId)) {
            // 게시글과 연결된 기술 스택 제거
            List<PostTechStack> postTechStacks = postTechStackRepository.findByPostId(postId);
            postTechStackRepository.deleteAll(postTechStacks);

            // 게시글과 연결된 포지션 제거
            List<PostPosition> postPositions = postPositionRepository.findByPostId(postId);
            postPositionRepository.deleteAll(postPositions);

            postRepository.delete(post);
        } else if (!post.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.POST_ACCESS_DENIED,"해당 게시글의 삭제 권한이 없습니다.");
        }

    }

    //merge 된 부분
    @Override
    public PostResponse completePost(Long postId, Long userId, String githubLink) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        post.updateStatus(Status.COMPLETED);

        portfolioService.createPortfolio(userId, postId, githubLink);

        // 추후 가져오는 로직 연결 필요
        //List<TechStackDto> techStackDtos = new ArrayList<>();
        List<TechStackDto> techStackDtos = findTechStacksByPostId(postId);
        //List<PositionDto> positionDtos = new ArrayList<>();
        List<PositionDto> positionDtos = findPositionsByPostId(postId);

        PostDto postDto = PostDto.from(post, techStackDtos, positionDtos);
        return PostResponse.from(postDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> findPostsWithFilters(String keyword, RecruitType recruitType, ProgressMethod progressMethod, CultureFit cultureFit, Long positionId, List<Long> techStackIds, Pageable pageable) {
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

        // 응답 변환
        return posts.map(post -> {
            // 각 게시글에 연결된 기술 스택과 포지션 조회
            List<TechStackDto> techStackDtos = findTechStacksByPostId(post.getId());
            List<PositionDto> positionDtos = findPositionsByPostId(post.getId());

            // DTO 변환
            PostDto postDto = PostDto.from(post, techStackDtos, positionDtos);
            return PostResponse.from(postDto);
        });
    }
}
