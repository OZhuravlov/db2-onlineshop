package com.study.onlineshop.web.controller;

import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.service.UserService;
import com.study.onlineshop.web.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Controller
public class SessionController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Value("${web.sessionMaxDurationInSeconds}")
    private int cookieMaxAge;

    PageGenerator pageGenerator = PageGenerator.instance();

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    @ResponseBody
    public String getRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        String page = pageGenerator.getPage("register", parameters);
        return page;
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String verifyPassword = request.getParameter("verify_password");
        System.out.println(login + " : " + password + " : " + verifyPassword);
        if (password != null && !password.isEmpty() && password.equals(verifyPassword)) {
            userService.add(login, password);
            Session session = securityService.login(login, password);
            if (session != null) {
                Cookie cookie = new Cookie("user-token", session.getToken());
                cookie.setMaxAge(cookieMaxAge);
                response.addCookie(cookie);
                response.sendRedirect("/");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.sendRedirect("/register");
        }
    }


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String getLogin() throws IOException {
        HashMap<String, Object> parameters = new HashMap<>();
        String page = pageGenerator.getPage("login", parameters);
        return page;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Session session = securityService.login(login, password);
        if (session != null) {
            Cookie cookie = new Cookie("user-token", session.getToken());
            cookie.setMaxAge(60 * 60 * 5);
            response.addCookie(cookie);
            response.sendRedirect("/");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }
}
