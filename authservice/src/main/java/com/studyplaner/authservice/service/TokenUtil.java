package com.studyplaner.authservice.service;

import com.studyplaner.authservice.Error.CustomTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class TokenUtil {

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public final static long TOKEN_VALIDATION_SECOND = 1800L * 1000;
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 24 * 2;

    public final static String ACCESS_TOKEN = "AccessToken";

    public Key getSignKey(String secretKey){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) throws ExpiredJwtException {

        Key key = getSignKey(SECRET_KEY);

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
        Key key = getSignKey(SECRET_KEY);

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
