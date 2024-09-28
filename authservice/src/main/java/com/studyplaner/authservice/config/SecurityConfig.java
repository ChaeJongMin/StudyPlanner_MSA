package com.studyplaner.authservice.config;

import com.studyplaner.authservice.Auth.CustomAuthenticationEntryPoint;
import com.studyplaner.authservice.Auth.IssueAuthenticationFilter;
import com.studyplaner.authservice.Auth.LoginAuthenticationFilter;
import com.studyplaner.authservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final ResponseTokenUtil responseTokenUtil;
    private final UserServiceImpl userServiceImpl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, IssueAuthenticationFilter issueAuthenctaionFilter) throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter =
                new LoginAuthenticationFilter(tokenUtil, cookieUtil,redisUtil,responseTokenUtil);
        loginAuthenticationFilter.setFilterProcessesUrl("/auth-service/login");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers( "/reissue","/logout","/login").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(conf -> conf.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(getIssueAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public IssueAuthenticationFilter getIssueAuthenticationFilter(){
        return new IssueAuthenticationFilter(userServiceImpl,tokenUtil,redisUtil,responseTokenUtil);
    }


}
