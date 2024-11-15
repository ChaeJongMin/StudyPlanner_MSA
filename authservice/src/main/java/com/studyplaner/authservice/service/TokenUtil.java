package com.studyplaner.authservice.service;

import com.studyplaner.authservice.Error.CustomTokenException;
import com.studyplaner.authservice.config.MyProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUtil {
    private final MyProperties myProperties;

    public final static String ACCESS_TOKEN = "AccessToken";

    public Key getSignKey(String secretKey){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        Key key = getSignKey(myProperties.getSecret());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpiredTime(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String doGenerateToken(String userId, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        Key key = getSignKey(myProperties.getSecret());

        return Jwts.builder()
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean isTokenExpired(String token, Claims claims){
        final Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token){
        try{
            Claims claims =  extractAllClaims(token);
            return !isTokenExpired(token,claims);
        } catch (MalformedJwtException |
                  UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException jwtException) {
            throw new CustomTokenException(jwtException.getMessage(), jwtException.getClass().getSimpleName());
        }
    }
}
