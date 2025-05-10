package com.eum.ai.service.impl;

import com.eum.ai.client.GeminiClient;
import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.global.exception.CustomException;
import com.eum.global.exception.ErrorCode;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CultureFitRecommendationServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private GeminiClient geminiClient;

    @InjectMocks
    private CultureFitRecommendationServiceImpl cultureFitService;

    private CultureFitRequest testRequest;
    private Post mockPost;

    @BeforeEach
    void setUp() {
        testRequest = new CultureFitRequest(
                "혼자 일한 후 결과를 공유하는 방식",
                "미리 계획하고 일찍 끝내려고 한다",
                "먼저 스스로 고민한 뒤 해결책을 공유한다",
                "필요한 경우에만 간결히 소통",
                "서로 타협점을 찾으려 한다",
                "능력에 따라 효율적으로 배분"
        );

        mockPost = mock(Post.class);
    }

    @Test
    void recommendCultureFitSuccessTest() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));
        when(geminiClient.requestCultureFit(testRequest)).thenReturn(Mono.just(CultureFit.AUTONOMOUS));

        StepVerifier.create(cultureFitService.recommendCultureFit(1L, testRequest))
                .expectNext(CultureFit.AUTONOMOUS)
                .verifyComplete();

        verify(postRepository).findById(1L);
        verify(mockPost).updateCultureFit(CultureFit.AUTONOMOUS);
        verify(postRepository).save(mockPost);
        verify(geminiClient).requestCultureFit(testRequest);
    }

    @Test
    void recommendCultureFitPostNotFound() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        StepVerifier.create(cultureFitService.recommendCultureFit(99L, testRequest))
                .expectErrorMatches(throwable ->
                        throwable instanceof CustomException &&
                                ((CustomException) throwable).getErrorCode() == ErrorCode.POST_NOT_FOUND
                )
                .verify();

        verify(postRepository).findById(99L);
        verifyNoInteractions(geminiClient);
    }

    @Test
    void previewCultureFitTest() {
        // given
        when(geminiClient.requestCultureFit(testRequest)).thenReturn(Mono.just(CultureFit.AUTONOMOUS));

        // when & then
        StepVerifier.create(cultureFitService.previewCultureFit(testRequest))
                .expectNext(CultureFit.AUTONOMOUS)
                .verifyComplete();

        verify(geminiClient).requestCultureFit(testRequest);
        verifyNoInteractions(postRepository);
    }

}
