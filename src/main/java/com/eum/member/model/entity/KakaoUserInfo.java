package com.eum.member.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserInfo {
    private String id; // Member에서 authId로 사용
    private String nickname;
}
