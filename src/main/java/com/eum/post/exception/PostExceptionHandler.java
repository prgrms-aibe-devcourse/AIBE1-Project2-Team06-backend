package com.eum.post.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class PostExceptionHandler {

  // Enum 타입 변환 등 JSON 파싱 오류 처리
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    log.error("JSON 파싱 오류: {}", ex.getMessage());

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Bad Request");

    // Enum 타입 오류 검출
    if (ex.getMessage().contains("not one of the values accepted for Enum class")) {
      String fieldName = extractEnumFieldName(ex.getMessage());
      response.put("message", "잘못된 Enum 값입니다: " + fieldName);
      response.put("detail", "유효한 값을 입력해주세요.");
    }
    // 날짜 형식 오류 검출
    else if (ex.getCause() instanceof DateTimeParseException) {
      response.put("message", "잘못된 날짜 형식입니다.");
      response.put("detail", "YYYY-MM-DD 형식으로 입력해주세요.");
    }
    // 기타 JSON 파싱 오류
    else {
      response.put("message", "요청 본문을 파싱할 수 없습니다.");
      response.put("detail", "JSON 형식이 잘못되었습니다.");
    }

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // 엔티티 찾을 수 없음 (404)
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleEntityNotFound(EntityNotFoundException ex) {
    log.error("엔티티 찾을 수 없음: {}", ex.getMessage());

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.NOT_FOUND.value());
    response.put("error", "Not Found");
    response.put("message", ex.getMessage());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  // 잘못된 인자 (400)
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
    log.error("잘못된 인자: {}", ex.getMessage());

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Bad Request");
    response.put("message", ex.getMessage());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // 그 외 모든 예외 (500)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
    log.error("서버 오류: {}", ex.getMessage(), ex);

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", "Internal Server Error");
    response.put("message", "서버 내부 오류가 발생했습니다.");

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
}
