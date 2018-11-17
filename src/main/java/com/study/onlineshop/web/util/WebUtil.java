package com.study.onlineshop.web.util;

import javax.servlet.http.Cookie;

public class WebUtil {

    public static String getTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    String token = cookie.getValue();
                    if (token.isEmpty()) {
                        return null;
                    } else {
                        return token;
                    }
                }
            }
        }
        return null;
    }

    public static Cookie createCookieFromToken(String token, int cookieMaxAge){
        Cookie cookie = new Cookie("user-token", token);
        cookie.setMaxAge(cookieMaxAge);
        return cookie;
    }
}
