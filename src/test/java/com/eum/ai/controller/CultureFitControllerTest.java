package com.eum.ai.controller;

import com.eum.ai.client.GeminiClient;
import com.eum.ai.model.dto.request.CultureFitRequest;
import com.eum.ai.service.CultureFitRecommendationService;
import com.eum.ai.service.impl.CultureFitRecommendationServiceImpl;
import com.eum.post.model.entity.Post;
import com.eum.post.model.entity.enumerated.CultureFit;
import com.eum.post.model.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CultureFitControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private CultureFitRecommendationService cultureFitService;

    @InjectMocks
    private CultureFitController cultureFitController;

    private final CultureFitRequest cultureFitRequest = new CultureFitRequest(
            "혼자 일한 후 결과를 공유하는 방식",
            "미리 계획하고 일찍 끝내려고 한다",
            "먼저 스스로 고민한 뒤 해결책을 공유한다",
            "필요한 경우에만 간결히 소통",
            "서로 타협점을 찾으려 한다",
            "능력에 따라 효율적으로 배분"
    );

    @BeforeEach
    public void setup() {
        this.webTestClient = WebTestClient.bindToController(cultureFitController).build();
    }

    @Test
    public void testRecommendCultureFit() {
        // 단일 요청 테스트
        Long postId = 1L;

        Mockito.when(cultureFitService.recommendCultureFit(Mockito.eq(postId), any(CultureFitRequest.class)))
                .thenReturn(Mono.just(CultureFit.AUTONOMOUS));

        webTestClient.post()
                .uri("/api/v1/culture-fit/{postId}/recommend", postId)
                .bodyValue(cultureFitRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CultureFit.class)
                .isEqualTo(CultureFit.AUTONOMOUS);

        Mockito.verify(cultureFitService).recommendCultureFit(Mockito.eq(postId), any(CultureFitRequest.class));
    }

    /**
     * WebFlux 기반 서비스가 비동기적으로 요청을 처리하는지 확인하는 테스트입니다.
     * 단, 내부에서 동기식 JPA를 사용하므로 완전한 논블로킹 환경은 아님을 감안해야 합니다.
     */
    @Test
    public void testMultipleConcurrentRequests() throws InterruptedException {
        // 여러 요청이 동시에 처리되는지 테스트
        int requestCount = 10;
        CountDownLatch latch = new CountDownLatch(requestCount);
        AtomicInteger completedCount = new AtomicInteger(0);

        // 모든 요청에 대해 0.5초의 지연을 갖는 응답을 설정
        Mockito.when(cultureFitService.recommendCultureFit(
                        Mockito.anyLong(), any(CultureFitRequest.class)))
                .thenAnswer(invocation -> {
                    // 각 요청은 0.5초 걸리도록 지연 설정
                    return Mono.just(CultureFit.AUTONOMOUS)
                            .delayElement(Duration.ofMillis(500))
                            .doOnSuccess(result -> {
                                completedCount.incrementAndGet();
                                latch.countDown();
                            });
                });


        // 시작 시간 기록
        long startTime = System.currentTimeMillis();

        // 여러 요청 동시에 실행
        List<Mono<CultureFit>> responses = new ArrayList<>();
        for (int i = 0; i < requestCount; i++) {
            Long postId = (long) i;
            // 컨트롤러를 통한 요청 직접 실행 (각 요청은 subscribe하지 않으면 실행되지 않음)
            responses.add(cultureFitController.recommendCultureFit(postId, cultureFitRequest));
        }

        // 각 응답 구독 - 이렇게 해야 Mono가 실제로 실행됨
        for (Mono<CultureFit> response : responses) {
            response.subscribe();
        }

        // 모든 요청이 완료될 때까지 대기
        boolean completed = latch.await(3, TimeUnit.SECONDS);

        // 종료 시간 기록
        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;

        // 결과 검증
        assertTrue(completed, "모든 요청이 시간 내에 완료되어야 합니다");
        assertEquals(completedCount.get(), requestCount, "모든 요청이 완료되어야 합니다");

        // 비동기 방식이라면 총 소요 시간은 각 요청의 처리 시간(0.5초)보다 조금 더 길어야 함
        // 동기 방식이었다면 총 소요 시간은 약 (요청 수 * 0.5초) = 5초가 될 것이다.
        System.out.println("총 소요 시간: " + totalDuration + "ms");
        assertTrue(totalDuration < requestCount * 500,
                "비동기 처리: 총 소요 시간이 각 요청 처리 시간의 합보다 짧아야 합니다. 실제 소요 시간: " + totalDuration + "ms");

        // 서비스 메소드가 정확히 10번 호출되었는지 검증
        Mockito.verify(cultureFitService, Mockito.times(requestCount))
                .recommendCultureFit(Mockito.anyLong(), any(CultureFitRequest.class));
    }

    @Test
    void SingleThreadNonblockingTest() {
        //given
        PostRepository mockPostRepo = mock(PostRepository.class);
        GeminiClient mockGemini = mock(GeminiClient.class);

        Post testPost = mock(Post.class);
        when(mockPostRepo.findById(any())).thenReturn(Optional.of(testPost));
        doNothing().when(testPost).updateCultureFit(any());
        when(mockPostRepo.save(any())).thenReturn(testPost);

        when(mockGemini.requestCultureFit(any())).thenAnswer(invocation -> {
            System.out.println("[GeminiClient] Thread: " + Thread.currentThread().getName());
            return Mono.just(CultureFit.AUTONOMOUS)
                    .doOnSubscribe(sub -> System.out.println("[Mono subsribe] Thread: " + Thread.currentThread().getName()))
                    .doOnNext(v -> System.out.println("[Mono emit] Thread: " + Thread.currentThread().getName()));
        });

        CultureFitRecommendationServiceImpl service = new CultureFitRecommendationServiceImpl(mockPostRepo, mockGemini);

        Mono<CultureFit> result = Mono.defer(() -> {
            System.out.println("[Test] Thread before service :" + Thread.currentThread().getName());
            return service.recommendCultureFit(1L, cultureFitRequest)
                    .publishOn(Schedulers.immediate())
                    .doOnNext(cf -> System.out.println("[Test] Thread after service :" +Thread.currentThread().getName()));
        });

        //when & then
        StepVerifier.create(result)
                .expectNext(CultureFit.AUTONOMOUS)
                .verifyComplete();
    }


}