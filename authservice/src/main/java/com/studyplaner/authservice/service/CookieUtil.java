package com.studyplaner.authservice.service;

import com.studyplaner.authservice.config.MyProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final MyProperties myProperties;

    public Cookie createCookie(String cookieName, String value){
        Cookie cookie = new Cookie(cookieName, value);

        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)myProperties.getAccessTokenValidityInSeconds());
        cookie.setPath("/");
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest req, String cookieName){
        final Cookie[] cookies = req.getCookies();
        if(cookies == null) return null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(cookieName))
                return cookie;
        }
        return null;
    }

}
