package com.footballgg.server.config;

import com.footballgg.server.jwt.JwtAuthenticationFilter;
import com.footballgg.server.jwt.JwtTokenProvider;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtTokenProvider jwtTokenProvider;
    @Bean
    public SecurityFilterChain filterChain(final @NotNull HttpSecurity http) throws Exception {
        // 스프링부트 3.1.x~ 시큐리티 설정 방식이 변경됨. .and()를 사용하지 않음
        http.httpBasic(HttpBasicConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(configurer-> // 세션 사용안해서 STATELESS 상태로 설정
                configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize->
                authorize
                        .requestMatchers("/login","/join","/index","/images/**").permitAll() // 페이지
                        .requestMatchers("/user/login/**","/user/join/**","/email/send").permitAll() // API
                        .requestMatchers("/user/profile/**","/user/test").hasRole("USER")
                        .requestMatchers("/**").permitAll() // CSS, JS 파일 허용
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login	// form 방식 로그인 사용
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(withDefaults())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
