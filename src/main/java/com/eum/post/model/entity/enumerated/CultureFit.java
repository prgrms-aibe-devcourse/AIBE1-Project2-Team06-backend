package com.eum.post.model.entity.enumerated;

import lombok.Getter;

@Getter
public enum CultureFit {
    AUTONOMOUS, // 자율형
    PLANNER, // 계획형
    COMMUNICATIVE, //소통 협업형
    PRACTICAL, //실용 협업형
    HARMONY, //조화 중시형
    DIRECTIVE; //지시 기반형
}
