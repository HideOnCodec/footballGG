package com.footballgg.server.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
public class EmailLoginDto {
    @Email
    private String email;
    @NotNull
    private String password;
}
