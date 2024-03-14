package com.footballgg.server.user.controller;

import com.footballgg.server.user.domain.User;
import com.footballgg.server.user.dto.EmailLoginRequestDto;
import com.footballgg.server.user.dto.EmailLoginResponseDto;
import com.footballgg.server.user.dto.EmailJoinRequestDto;
import com.footballgg.server.user.security.service.SecurityUtil;
import com.footballgg.server.user.service.EmailAuthService;
import com.footballgg.server.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Controller
@Slf4j
public class UserController {
    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final EmailAuthService emailAuthService;

    /** 메인 뷰 */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /** 회원가입 뷰 */
    @GetMapping("/join")
    public String join(Model model)
    {
        model.addAttribute("emailJoinRequestDto",new EmailJoinRequestDto());
        return "join";
    }

    /** 이메일 회원가입
     * [POST] /user/join/email
     * @Body  emailRequestDto : nickname,email, password
     */
    @PostMapping("/user/join/email")
    public String joinByEmail(@Valid EmailJoinRequestDto emailJoinRequestDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors())
            return "join";

        if(userService.isDuplicatedEmail(emailJoinRequestDto.getEmail())){
            model.addAttribute("msg","중복된 이메일입니다.");
            return "join";
        }

        // 이메일 인증 실패 시
        if(!emailAuthService.verifyAuthCode(emailJoinRequestDto.getEmail(), emailJoinRequestDto.getCode())) {
            model.addAttribute("msg", "이메일 인증에 실패하였습니다.");
            return "join";
        }
        userService.emailSignUp(emailJoinRequestDto);
        return "redirect:/login";
    }

    /** 로그인 뷰 */
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user, Model model) {
        /*이미 로그인된 사용자일 경우 인덱스 페이지로 강제이동.*/
        if (user != null) {
            log.info(user.getNickname() + "님이 로그인 페이지로 이동을 시도함. -> index 페이지로 강제 이동 함.");
            return "redirect:/index";
        }
        model.addAttribute("emailLoginRequestDto",new EmailLoginRequestDto());
        return "login";
    }

    /** 이메일 로그인
     * [POST] /user/login/email
     */
    @PostMapping("/user/login/email") // @Valid Dto에 정의된 lombok에 맞게 객체를 검증해줌.
    public String loginByEmail(@Valid EmailLoginRequestDto emailLoginRequestDto, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors())
            return "login";
        log.info("username = {}, password = {}",emailLoginRequestDto.getEmail(),emailLoginRequestDto.getPassword());

        EmailLoginResponseDto emailLoginResponseDto = userService.emailLogin(emailLoginRequestDto,response);
        log.info("request username = {}, password = {}", emailLoginRequestDto.getEmail(), emailLoginRequestDto.getPassword());
        log.info("jwtToken accessToken = {} refreshToken = {}", emailLoginResponseDto.getAccessToken(),emailLoginResponseDto.getRefreshToken());
        return "redirect:/index";
    }

    /** 로그아웃 API*/
    @GetMapping("/user/logout")
    public String logout(@CookieValue(value = "Authorization", defaultValue = "", required = false) Cookie jwtCookie,
                         HttpServletResponse response) {
        /*jwt 쿠키를 가지고와서 제거한다.*/
        jwtCookie.setValue(null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "redirect:/login";
    }

    /** 테스트
     * [POST] /user/test
     * @Header Authociation : accessToken
     */
    @PostMapping("/user/test")
    public String test() {
        return securityUtil.getLoginUsername();
    }
}
