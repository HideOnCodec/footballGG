package com.footballgg.server.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class EmailLoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
