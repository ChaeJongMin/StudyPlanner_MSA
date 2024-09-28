package com.studyplaner.gatewayservice.Filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyplaner.gatewayservice.Util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthorizationCookieFilter extends AbstractGatewayFilterFactory<AuthorizationCookieFilter.Config> {

    private final TokenUtil tokenUtil;

    @Autowired
    public AuthorizationCookieFilter(TokenUtil tokenUtil) {
        super(Config.class);
        this.tokenUtil = tokenUtil;
    }


    static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            //쿠키에서 액세스 토큰 가져오기
            String accessToken = "";
            HttpCookie accessTokenCookie = exchange.getRequest().getCookies().getFirst("accessToken");
            if (accessTokenCookie == null) {
                return onError(exchange, "Token does not exist.", HttpStatus.UNAUTHORIZED, "Token not authorization");
            }
            accessToken = accessTokenCookie.getValue();

            //액세스 토큰이 유효성 검사
            tokenUtil.validateJwtToken(accessToken);

            return chain.filter(exchange);
        };

    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMsg, HttpStatus httpStatus, String code) {
        log.error(errorMsg);

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String jsonResponse = String.format("{\"message\":\"%s\", \"code\":\"%s\"}", errorMsg, code);
        DataBuffer buffer = response.bufferFactory().wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
