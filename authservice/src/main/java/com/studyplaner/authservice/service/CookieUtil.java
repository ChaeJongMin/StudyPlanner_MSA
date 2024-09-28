package com.studyplaner.authservice.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public Cookie createCookie(String cookieName, String value){
        Cookie cookie = new Cookie(cookieName, value);

        cookie.setHttpOnly(true);
        cookie.setMaxAge((int)TokenUtil.TOKEN_VALIDATION_SECOND);
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
