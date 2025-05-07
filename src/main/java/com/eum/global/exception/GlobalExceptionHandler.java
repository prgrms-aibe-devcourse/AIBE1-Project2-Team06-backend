package com.eum.global.exception;

import com.eum.global.exception.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // JSON 파싱 오류 (400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("JSON 파싱 오류: {}", ex.getMessage());

        String message = "요청 본문을 파싱할 수 없습니다";
        String detail = "JSON 형식이 잘못되었습니다";

        // Enum 타입 오류 검출
        if (ex.getMessage().contains("not one of the values accepted for Enum class")) {
            String fieldName = extractEnumFieldName(ex.getMessage());
            message = "잘못된 Enum 값입니다: " + fieldName;
            detail = "유효한 값을 입력해주세요";
        }
        // 날짜 형식 오류 검출
        else if (ex.getCause() instanceof DateTimeParseException) {
            message = "잘못된 날짜 형식입니다";
            detail = "YYYY-MM-DD 형식으로 입력해주세요";
        }

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                detail
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 404 - 핸들러 없음
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex, HttpServletRequest request) {
        log.error("핸들러 없음: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND,
                "요청한 리소스를 찾을 수 없습니다: " + ex.getRequestURL(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 엔티티 찾을 수 없음 (404)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex, HttpServletRequest request) {
        log.error("엔티티 찾을 수 없음: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 잘못된 인자 (400)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.error("잘못된 인자: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 그 외 모든 예외 (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(
            Exception ex, HttpServletRequest request) {
        log.error("서버 오류: {}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "서버 내부 오류가 발생했습니다",
                request.getRequestURI(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Enum 필드 이름 추출 메소드
    private String extractEnumFieldName(String errorMessage) {
        // 예시: "Cannot deserialize value of type `com.eum.post.model.entity.enumerated.RecruitType` from String..."
        try {
            // Enum 클래스 이름 추출
            String enumClassPath = errorMessage.split("type `")[1].split("`")[0];
            String enumClassName = enumClassPath.substring(enumClassPath.lastIndexOf('.') + 1);

            // 필드 이름으로 변환 (예: RecruitType -> recruitType)
            return Character.toLowerCase(enumClassName.charAt(0)) + enumClassName.substring(1);
        } catch (Exception e) {
            return "알 수 없는 필드";
        }
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("Custom 예외 : {} - {}", errorCode.getCode(), ex.getMessage());

        ErrorResponse response = ErrorResponse.of(
                errorCode.getStatus(),
                ex.getMessage(),
                request.getRequestURI(),
                errorCode.getCode()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}