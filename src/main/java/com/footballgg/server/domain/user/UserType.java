package com.footballgg.server.domain.user;

import lombok.Getter;

@Getter
public enum UserType {
    EMAIL("이메일 회원"), // 0
    KAKAO("카카오 회원"); // 1
    private final String key;

    UserType(String key) {
        this.key = key;
    }
}
