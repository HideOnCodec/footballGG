package com.footballgg.server.user.dto;

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
