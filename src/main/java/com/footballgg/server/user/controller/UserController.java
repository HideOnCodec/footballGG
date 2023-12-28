package com.footballgg.server.user.controller;

import com.footballgg.server.base.baseresponse.BaseResponse;
import com.footballgg.server.base.baseresponse.BaseResponseStatus;
import com.footballgg.server.user.domain.User;
import com.footballgg.server.user.dto.EmailLoginRequestDto;
import com.footballgg.server.user.dto.EmailLoginResponseDto;
import com.footballgg.server.user.dto.EmailRequestDto;
import com.footballgg.server.user.security.jwt.JwtToken;
import com.footballgg.server.user.security.service.SecurityUtil;
import com.footballgg.server.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    private final SecurityUtil securityUtil;
    /**
     * 이메일 회원가입
     * [POST] /user/join/email
     * @Body  emailRequestDto : nickname,email, password
     */
    @PostMapping("/join/email")
    public BaseResponse<User> joinByEmail(@Valid @RequestBody EmailRequestDto emailRequestDto){
        User user = userService.emailSignUp(emailRequestDto);
        if(user==null) // 이메일 중복일 경우
            return new BaseResponse<>(BaseResponseStatus.FAILED_DUPLICATED_EMAIL);

        return new BaseResponse<>(BaseResponseStatus.SUCCESS_EMAIL_SIGNUP);
    }

    /**
     * 이메일 로그인
     * [POST] /user/login/email
     * @Body emailLoginDto : email, password
     */
    @PostMapping("/login/email") // @Valid Dto에 정의된 lombok에 맞게 객체를 검증해줌.
    public BaseResponse<EmailLoginResponseDto> loginByEmail(@Valid @RequestBody EmailLoginRequestDto emailLoginRequestDto){
        EmailLoginResponseDto emailLoginResponseDto = userService.emailLogin(emailLoginRequestDto);
        if(emailLoginResponseDto == null){
            return new BaseResponse<>(BaseResponseStatus.FAILED_NOT_FOUND_USER);
        }
        log.info("request username = {}, password = {}", emailLoginRequestDto.getEmail(), emailLoginRequestDto.getPassword());
        log.info("jwtToken accessToken = {} refreshToken = {}", emailLoginResponseDto.getAccessToken(),emailLoginResponseDto.getRefreshToken());
        return new BaseResponse<>(emailLoginResponseDto,BaseResponseStatus.SUCCESS_EMAIL_LOGIN);
    }

    //TODO : Access Token 이 만료가 되면 여기로 요청을 보냄
    @PutMapping("/newAccess")
    public BaseResponse<JwtToken> issueAccessToken(HttpServletRequest request) {
        return new BaseResponse<>(userService.issueAccessToken(request),BaseResponseStatus.SUCCESS_REFRESH_TOKEN);
    }

    /**
     * 테스트
     * [POST] /user/test
     * @Header Authociation : accessToken
     */
    @PostMapping("/test")
    public String test() {
        return securityUtil.getLoginUsername();
    }
}
