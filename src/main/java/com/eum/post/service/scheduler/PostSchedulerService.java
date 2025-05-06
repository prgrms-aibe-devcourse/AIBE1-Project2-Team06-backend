package com.eum.post.service.scheduler;

import com.eum.post.model.entity.enumerated.Status;
import com.eum.post.model.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostSchedulerService {
    private final PostRepository postRepository;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updatePostStatus() {
        LocalDate today = LocalDate.now();

        // 모집중(RECRUITING) → 마감(CLOSED)로 변경
        int closedCount = postRepository.bulkUpdateStatusForExpiredPosts(
                Status.RECRUITING, Status.CLOSED, today);

        log.info("게시글 상태 업데이트 완료: {}개 마감", closedCount);
    }
}
