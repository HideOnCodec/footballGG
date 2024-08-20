package com.footballgg.server.controller.user;

import com.footballgg.server.dto.user.EmailJoinRequestDto;
import com.footballgg.server.service.user.EmailAuthService;
import com.footballgg.server.service.user.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Controller
@Slf4j
@RequiredArgsConstructor
public class EmailAuthController {
    private final EmailAuthService emailAuthService;
    private final UserService userService;
    /**
     * 이메일 인증 메일 전송
     * [GET] /email/send?email=email
     */
    @GetMapping("/email/send")
    public String sendEmailAuth(@RequestParam String email, Model model) throws MessagingException, UnsupportedEncodingException {
        model.addAttribute("emailJoinRequestDto",new EmailJoinRequestDto());
        model.addAttribute("isDuplicated","왜 안나옴");
        if(userService.isDuplicatedEmail(email)){
            log.info("중복된 이메일={}",email);
            model.addAttribute("msg","중복된 이메일입니다.");
            return "/join :: #email-auth";
        }
        emailAuthService.sendEmail(email);
        // 현재 시간 + 30분
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(30);
        model.addAttribute("success",true);
        return "/join :: #email-auth";
    }

}
