package com.footballgg.server.user.service;

import com.footballgg.server.user.domain.User;
import com.footballgg.server.user.dto.EmailLoginDto;
import com.footballgg.server.user.dto.EmailRequestDto;
import com.footballgg.server.user.repository.UserRepository;
import com.footballgg.server.user.security.jwt.JwtToken;
import com.footballgg.server.user.security.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User emailSignUp(EmailRequestDto emailRequestDto){
        Optional<User> user = userRepository.findByEmail(emailRequestDto.getEmail());
        if(user.isPresent())
            return null; // 이미 존재하는 메일일 경우

        User userResult = userRepository.save(emailRequestDto.toEntity());

        userResult.encodePassword(passwordEncoder);
        userResult.addUserAuthority();
        log.info(userResult.getPassword());
        return userResult;
    }

    @Transactional
    public JwtToken emailLogin(EmailLoginDto emailLoginDto){
        User user = userRepository.findByEmail(emailLoginDto.getEmail()).get();
        if(user == null) { // 존재하지 않는 회원일 경우
            return null;
        }
        if(!passwordEncoder.matches(emailLoginDto.getPassword(), user.getPassword())){
            return null; // 비밀번호가 일치하지 않을 경우
        }

        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().name());

        return jwtTokenProvider.createToken(user.getUsername(), roles);
    }
}
