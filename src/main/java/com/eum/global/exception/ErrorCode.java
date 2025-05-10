package com.eum.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통 오류
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력값입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "요청한 데이터를 찾을 수 없습니다"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "C004", "접근 권한이 없습니다"),

    // 회원 관련 오류
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "회원을 찾을 수 없습니다"),
    DUPLICATE_MEMBER_ID(HttpStatus.CONFLICT, "M002", "이미 존재하는 회원 ID입니다"),

    // 게시글 관련 오류
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "게시글을 찾을 수 없습니다"),
    INVALID_POST_STATUS(HttpStatus.BAD_REQUEST, "P002", "유효하지 않은 게시글 상태입니다"),
    POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "P003", "해당 게시글에 대한 권한이 없습니다"),
    POST_NOT_COMPLETE(HttpStatus.BAD_REQUEST, "P004", "완료된 프로젝트에 대해서만 리뷰를 작성할 수 있습니다."),


    // AI 서비스 관련 오류
    AI_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "A001", "AI 서비스 호출 중 오류가 발생했습니다"),

    // 리뷰 관련 오류
    SELF_REVIEW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "R001", "자기 자신에 대한 리뷰는 작성할 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "R002", "리뷰를 찾을 수 없습니다"),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "R003", "이미 리뷰를 작성했습니다"),
    REVIEW_TARGET_NOT_TEAM_MEMBER(HttpStatus.BAD_REQUEST, "R004", "리뷰 대상이 해당 팀의 멤버가 아닙니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
