package com.eum.post.model.entity.enumerated;

public enum ProgressMethod {
    ONLINE,
    OFFLINE,
    ALL;

    public static ProgressMethod fromString(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return ProgressMethod.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
