package com.eum.post.model.entity.enumerated;

public enum RecruitType{
    STUDY,
    PROJECT;

    public static RecruitType fromString(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return RecruitType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}


