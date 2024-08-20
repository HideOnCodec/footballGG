package com.footballgg.server.dto.user;

import com.footballgg.server.domain.user.User;
import com.footballgg.server.domain.user.Role;
import com.footballgg.server.domain.user.UserType;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class EmailJoinRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z1-9]{8,20}",
            message = "비밀번호는 영어와 숫자를 포함해서 8~20자리 이내로 입력해주세요")
    private String password;

    @NotBlank(message = "인증코드를 입력해주세요.")
    private String code;

    @Size(min=2)
    private String nickname;

    private String profileImage;

    @Builder
    public User toEntity(){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .role(Role.ROLE_USER)
                .userType(UserType.EMAIL)
                .build();
    }
}
