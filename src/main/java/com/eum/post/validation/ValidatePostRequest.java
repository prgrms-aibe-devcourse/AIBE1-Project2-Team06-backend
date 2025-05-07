package com.eum.post.validation;

import com.eum.post.model.dto.request.PostRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ValidatePostRequest {
    // 요청 유효성 검증 메소드
    public static void validatePostRequest(PostRequest request) {
        // 필수 필드 목록
        Map<String, Object> fields = new HashMap<>();
        fields.put("title", request.title());
        fields.put("content", request.content());
        fields.put("recruitType", request.recruitType());
        fields.put("recruitMember", request.recruitMember());
        fields.put("progressMethod", request.progressMethod());
        fields.put("period", request.period());
        fields.put("deadline", request.deadline());
        fields.put("linkType", request.linkType());
        fields.put("link", request.link());

        // 누락된 필드 확인
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException(entry.getKey() + " 필드는 필수 입력 항목입니다.");
            }
        }

        // 문자열 필드가 빈 값인지 확인
        if (request.title() != null && request.title().trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 비워둘 수 없습니다.");
        }
        if (request.content() != null && request.content().trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 비워둘 수 없습니다.");
        }
        if (request.link() != null && request.link().trim().isEmpty()) {
            throw new IllegalArgumentException("링크는 비워둘 수 없습니다.");
        }

        // recruitMember 값 확인
        if (request.recruitMember() != null && request.recruitMember() <= 0) {
            throw new IllegalArgumentException("모집 인원은 1명 이상이어야 합니다.");
        }

        // 날짜 유효성 검증
        if (request.deadline() != null && request.deadline().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("마감일은 현재 날짜 이후로 설정해야 합니다.");
        }
    }
}
