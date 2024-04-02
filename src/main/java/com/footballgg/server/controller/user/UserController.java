package com.footballgg.server.controller.user;

import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.user.EmailLoginRequestDto;
import com.footballgg.server.dto.user.EmailLoginResponseDto;
import com.footballgg.server.dto.user.EmailJoinRequestDto;
import com.footballgg.server.service.user.security.SecurityUtil;
import com.footballgg.server.service.user.EmailAuthService;
import com.footballgg.server.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
    public String joinForm(Model model)
    {
        model.addAttribute("emailJoinRequestDto",new EmailJoinRequestDto());
        return "join";
    }

    /** 이메일 회원가입
     * [POST] /user/join/email
     * @Body  emailRequestDto : nickname,email, password
     */
    @PostMapping("/user/join/email")
    public String joinByEmail(@Valid EmailJoinRequestDto emailJoinRequestDto, BindingResult bindingResult){
        // 필드 에러
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "join";
        }

        // 글로벌 에러
        if(userService.isDuplicatedEmail(emailJoinRequestDto.getEmail())){
            bindingResult.reject("duplicated");
            return "join";
        }

        // 이메일 인증 실패 시
        if(!emailAuthService.verifyAuthCode(emailJoinRequestDto.getEmail(), emailJoinRequestDto.getCode())) {
            bindingResult.reject("failedAuthentication");
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
        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "login";
        }

        EmailLoginResponseDto emailLoginResponseDto = userService.emailLogin(emailLoginRequestDto,response);
        if(emailLoginResponseDto == null){
            bindingResult.reject("failedLogin");
            return "login";
        }
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
        return securityUtil.getLoginUser().getNickname();
    }
}
