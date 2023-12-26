package com.footballgg.server.user.dto;

import com.footballgg.server.user.domain.User;
import com.footballgg.server.user.usertype.Role;
import com.footballgg.server.user.usertype.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data // getter/setter, requiredArgsController, ToString 등 합쳐놓은 세트
@Builder
@AllArgsConstructor
public class EmailRequestDto {

    @NotEmpty(message = "이메일을 입력해주세요")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = " ^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
            message = "8자 이상이며 최대 20자까지 허용. 반드시 숫자, 문자 포함")
    private String password;

    @NotEmpty(message = "닉네임을 입력해주세요")
    @Size(min=2, message = "닉네임은 최소 두 글자 이상입니다")
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
