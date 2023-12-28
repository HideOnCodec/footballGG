package com.footballgg.server.user.service;

import com.footballgg.server.user.security.service.SecurityUtil;
import org.junit.jupiter.api.Test;

public class UserLoginTest {
    UserService userService;
    SecurityUtil securityUtil;
    @Test
    void loginUserInfo(){
        String userEmail = securityUtil.getLoginUsername();
        System.out.println("Username : "+userEmail);
    }
}
