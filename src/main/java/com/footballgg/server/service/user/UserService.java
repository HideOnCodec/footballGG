package com.footballgg.server.service.user;

import com.footballgg.server.domain.user.User;
import com.footballgg.server.dto.user.EmailLoginRequestDto;
import com.footballgg.server.dto.user.EmailLoginResponseDto;
import com.footballgg.server.dto.user.EmailJoinRequestDto;
import com.footballgg.server.repository.user.UserRepository;
import com.footballgg.server.jwt.JwtToken;
import com.footballgg.server.jwt.JwtTokenProvider;
import com.footballgg.server.service.user.security.SecurityUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public boolean isDuplicatedEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    @Transactional
    public User emailSignUp(EmailJoinRequestDto emailJoinRequestDto){
        User userResult = userRepository.save(emailJoinRequestDto.toEntity());

        userResult.encodePassword(passwordEncoder);
        userResult.addUserAuthority();
        log.info(userResult.getPassword());
        return userResult;
    }

    @Transactional
    public EmailLoginResponseDto emailLogin(EmailLoginRequestDto emailLoginRequestDto, HttpServletResponse response){
        User user = userRepository.findByEmail(emailLoginRequestDto.getEmail()).orElse(null);
        if(user == null) { // 존재하지 않는 회원일 경우
            return null;
        }
        if(!passwordEncoder.matches(emailLoginRequestDto.getPassword(), user.getPassword())){
            return null; // 비밀번호가 일치하지 않을 경우
        }

        List<String> roles = new ArrayList<>();
        roles.add(user.getRole().name());

        String accessToken = jwtTokenProvider.createToken(user.getUsername(), roles);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        /*ACCESS TOKEN 쿠키로 발급*/
        Cookie accessCookie = new Cookie("Authorization", accessToken);
        accessCookie.setMaxAge(60 * 60); // 1시간 동안 유효
        accessCookie.setPath("/");
        accessCookie.setDomain("localhost");
        accessCookie.setSecure(false);

        response.addCookie(accessCookie);

        /*REFRESH TOKEN 쿠키로 발급*/
//        Cookie refreshCookie = new Cookie("Authorization", accessToken);
//        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 동안 유효
//        refreshCookie.setPath("/");
//        refreshCookie.setDomain("localhost");
//        refreshCookie.setSecure(false);
//
//        response.addCookie(refreshCookie);

        return EmailLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 만료된 토큰 재발급
    @Transactional
    public JwtToken issueAccessToken(HttpServletRequest request) {
        //TODO : 만료된 accessToken 과 refreshToken 을 가져옴
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = jwtTokenProvider.resolveToken(request);

        //TODO : accessToken 이 만료되었으면
        if(!jwtTokenProvider.validateToken(accessToken)) {
            log.info("accessToken is expired");
            //TODO : 만약 refreshToken 이 유효하다면
            if(jwtTokenProvider.validateToken(refreshToken)) {
                log.info("refreshToken is validate ");

                //TODO : DB에 저장해두었던 refreshToken 을 불러오고 새로운 Access Token 을 생성
                User user = userRepository.findByEmail(jwtTokenProvider.getUserEmail(refreshToken))
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

                List<String> roles = new ArrayList<>();
                roles.add(user.getRole().name());

                //TODO : 만약 DB refreshToken 와 요청한 refreshToken 가 같다면
                if(refreshToken.equals(user.getRefreshToken())) {
                    //TODO : 새로운 accessToken 생성
                    accessToken = jwtTokenProvider.createToken(user.getEmail(), roles);
                }
                else {
                    log.info("토큰이 변조되었습니다.");
                }
            }
            else {
                log.info("Refresh Token 이 유효하지 않습니다.");
            }
        }
        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
