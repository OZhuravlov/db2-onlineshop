package com.study.onlineshop.security;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.service.UserService;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class SecurityService {
    private List<Session> sessionList = new ArrayList<>();

    private UserService userService;

    public Session login(String login, String password) {
        User user = userService.getUser(login, password);
        if (user != null) {
            Session session = new Session();
            session.setUser(user);
            session.setToken(UUID.randomUUID().toString());

            session.setExpireDate(LocalDateTime.now().plusHours(5));
            sessionList.add(session);
            return session;
        }
        return null;
    }

    public void logout(String token) {
        for (Session session : sessionList) {
            if (session.getToken() != null && session.getToken().equals(token)) {
                sessionList.remove(session);
                break;
            }
            if (session.getExpireDate().isBefore(LocalDateTime.now())) {
                sessionList.remove(session);
            }
        }
    }

    public Session getSession(String token) {
        for (Session session : sessionList) {
            if (session.getExpireDate().isBefore(LocalDateTime.now())) {
                sessionList.remove(session);
            } else if (session.getToken().equals(token)) {
                return session;
            }
        }
        return null;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getValidatedToken(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("user-token")) {
                    String token = cookie.getValue();
                    if (token != null && !token.isEmpty()) {
                        Session session = getSession(token);
                        if (session != null) {
                            return token;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        return null;
    }

    public boolean checkTokenPermissions(String token, EnumSet roles) {
        Session session = getSession(token);
        return session != null && roles.contains(session.getUser().getUserRole());
    }

}
