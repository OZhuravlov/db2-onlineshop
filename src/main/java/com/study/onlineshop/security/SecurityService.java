package com.study.onlineshop.security;

import com.study.onlineshop.PropertiesService;
import com.study.onlineshop.ServiceLocator;
import com.study.onlineshop.entity.User;
import com.study.onlineshop.service.UserService;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.*;

public class SecurityService {
    private List<Session> sessionList = new ArrayList<>();

    private UserService userService;

    public Session login(String login, String password) {
        User user = userService.getUser(login, password);
        if (user != null) {
            Session session = new Session();
            session.setUser(user);
            session.setToken(UUID.randomUUID().toString());

            PropertiesService propertiesService = ServiceLocator.getServiceLocator().getService(PropertiesService.class);
            Long maxSessionDurationInSeconds = Long.parseLong(propertiesService.getProperty("web.sessionMaxDurationInSeconds"));
            session.setExpireDate(LocalDateTime.now().plusSeconds(maxSessionDurationInSeconds));
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
        Iterator<Session> iterator = sessionList.iterator();
        while(iterator.hasNext()){
            Session session = iterator.next();
            if (session.getToken().equals(token)){
                if (session.getExpireDate().isBefore(LocalDateTime.now())) {
                    iterator.remove();
                    return null;
                } else  {
                    return session;
                }
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
