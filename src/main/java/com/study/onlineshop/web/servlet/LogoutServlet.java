package com.study.onlineshop.web.servlet;

import com.study.onlineshop.ServiceLocator;
import com.study.onlineshop.security.SecurityService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private SecurityService securityService;

    @Override
    public void init() throws ServletException {
        securityService = ServiceLocator.getServiceLocator().getService(SecurityService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("user-token")) {
                    securityService.logout(cookie.getValue());
                    cookie.setMaxAge(-1);
                    request.removeAttribute("session");
                }
            }
        }
        response.sendRedirect("/login");
    }
}

