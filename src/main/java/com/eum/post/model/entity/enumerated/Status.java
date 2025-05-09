package com.eum.post.model.entity.enumerated;

public enum Status {
    RECRUITING, // 게시글 모집중
    CLOSED, // 게시글 마감
    ONGOING, // 진행중
    COMPLETED; // 완료

    public static Status fromString(String value) {
        if (value == null || value.isEmpty()) return RECRUITING;
        try {
            return Status.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return RECRUITING;
        }
    }
}
