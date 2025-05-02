package com.eum.post.service.impl;

import com.eum.global.model.entity.Position;
import com.eum.global.model.entity.TechStack;
import com.eum.global.model.repository.PositionRepository;
import com.eum.global.model.repository.TechStackRepository;
import com.eum.post.model.dto.PositionDto;
import com.eum.post.model.dto.PostDto;
import com.eum.post.model.dto.TechStackDto;
import com.eum.post.model.dto.request.PostRequest;
import com.eum.post.model.dto.response.PostResponse;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.PostPosition;
import com.eum.post.model.entity.PostTechStack;
import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.PortfolioRepository;
import com.eum.post.model.repository.PostPositionRepository;
import com.eum.post.model.repository.PostRepository;
import com.eum.post.model.repository.PostTechStackRepository;
import com.eum.post.service.PortfolioService;
import com.eum.post.service.PostService;
import com.eum.post.validation.ValidatePostRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostTechStackRepository postTechStackRepository;
    private final PostPositionRepository postPositionRepository;
    private final PositionRepository positionRepository;
    private final TechStackRepository techStackRepository;

    private final PortfolioService portfolioService;

    @Override
    @Transactional
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
                    .orElseThrow(() -> new EntityNotFoundException(
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
                    .orElseThrow(() -> new EntityNotFoundException(
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
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));

        // 관련 기술 스택 조회
        List<TechStackDto> techStackDtos = findTechStacksByPostId(post.getId());

        // 관련 포지션 조회
        List<PositionDto> positionDtos = findPositionsByPostId(post.getId());

        // DTO 변환 및 반환
        PostDto postDto = PostDto.from(post, techStackDtos, positionDtos);
        return PostResponse.from(postDto);
    }

    /**
     * 전체 게시글 조회
     *
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> findByAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> {
            // 각 게시글에 연결된 기술 스택과 포지션 조회
            List<TechStackDto> techStackDtos = findTechStacksByPostId(post.getId());
            List<PositionDto> positionDtos = findPositionsByPostId(post.getId());

            // DTO 변환
            PostDto postDto = PostDto.from(post, techStackDtos, positionDtos);
            return PostResponse.from(postDto);
        });
    }

    //merge 된 부분
    @Override
    @Transactional
    public PostResponse completePost(Long postId, Long userId, String githubLink) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));

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
}
