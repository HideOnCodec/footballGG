package com.footballgg.server.service.user.security;

import com.footballgg.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityUtil {
    //SecurityContext에서 Authentication객체를 꺼내와서 이 객체를 통해 로그인한 username을 리턴해주는 간단한 유틸성 메소드
    public static String getLoginUsername(){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

    // 로그인한 유저 엔티티 조회
    public static User getLoginUser(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
