package com.footballgg.server.user.security.jwt;

import com.footballgg.server.user.security.service.CustomUserDetailService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider  {
    @Value("${jwt.secret-key}")
    private String secretKey;
    private final CustomUserDetailService customUserDetailService;
    private long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60l; //1시간

    private long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7l; //일주일
    // User 정보를 가지고 AccessToken을 생성하는 메서드
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userPk, List<String> roles) {
        // 권한 가져오기
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key/value 쌍으로 저장됩니다.
        Date now = new Date();
        // Access Token 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return accessToken;
    }
    // RefreshToken을 생성하는 메소드
    public String createRefreshToken(){
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        Claims claims = parseJwt(token);
        String s=claims.getSubject();

        UserDetails userDetails = customUserDetailService.loadUserByUsername(s);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        Claims claims = null;
        try {
            claims = parseJwt(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        return null;
    }

    public Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();

        return claims;
    }

    public String getUserEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
