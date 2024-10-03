package com.studyplaner.authservice.config;

import com.studyplaner.authservice.Auth.CustomAuthenticationEntryPoint;
import com.studyplaner.authservice.Auth.IssueAuthenticationFilter;
import com.studyplaner.authservice.Auth.LoginAuthenticationFilter;
import com.studyplaner.authservice.Repository.UserRepository;
import com.studyplaner.authservice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint,AuthenticationManager authenticationManager) throws Exception {

        LoginAuthenticationFilter loginAuthenticationFilter =
                new LoginAuthenticationFilter(tokenUtil, cookieUtil,redisUtil,responseTokenUtil,userServiceImpl);
        loginAuthenticationFilter.setFilterProcessesUrl("/login");
        loginAuthenticationFilter.setAuthenticationManager(authenticationManager);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll())
                .authenticationManager(authenticationManager)
                .exceptionHandling(conf -> conf.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(loginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new IssueAuthenticationFilter(userServiceImpl,tokenUtil,redisUtil,responseTokenUtil),UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // retrieve builder from httpSecurity
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

}
