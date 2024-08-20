package com.footballgg.server.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
public class EmailLoginRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
