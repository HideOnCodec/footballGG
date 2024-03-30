package com.footballgg.server.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
