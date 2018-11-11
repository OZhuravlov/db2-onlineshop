package com.study.onlineshop.web.service;

import javax.servlet.http.Cookie;

public class CookieService {

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
}
