package com.footballgg.server;

import com.footballgg.server.dto.user.EmailJoinRequestDto;
import com.footballgg.server.service.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final UserService userService;

    /**
     * 테스트용 데이터 추가
     */
//    @PostConstruct
//    public void init() {
//        EmailJoinRequestDto emailJoinRequestDto = new EmailJoinRequestDto();
//        emailJoinRequestDto.setEmail("tlsdmsgp33@naver.com");
//        emailJoinRequestDto.setCode("1234");
//        emailJoinRequestDto.setNickname("test");
//        emailJoinRequestDto.setPassword("test");
//
//        userService.emailSignUp(emailJoinRequestDto);
//    }
}
