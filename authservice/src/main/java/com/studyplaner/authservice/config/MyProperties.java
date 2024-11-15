package com.studyplaner.authservice.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ConfigurationProperties("jwt")
@Validated
public class MyProperties {
    @NotNull
    private String secret;

    @NotNull
    private long accessTokenValidityInSeconds;

    @NotNull
    private long refreshTokenValidityInSeconds;

//    public MyProperties(String secret, long seconds){
//        this.secret = secret;
//        this.token_validity_in_seconds = seconds;
//    }
    public MyProperties() {}
}
