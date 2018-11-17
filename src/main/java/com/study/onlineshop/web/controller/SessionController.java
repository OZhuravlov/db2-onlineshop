package com.study.onlineshop.web.controller;

import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.web.templater.PageGenerator;
import com.study.onlineshop.web.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Controller
public class SessionController {

    private SecurityService securityService;
    private int cookieMaxAge;

    PageGenerator pageGenerator = PageGenerator.instance();

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    @ResponseBody
    public String getRegister() {
        HashMap<String, Object> parameters = new HashMap<>();
        String page = pageGenerator.getPage("register", parameters);
        return page;
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String login,
                           @RequestParam String password,
                           @RequestParam("verify_password") String verifyPassword
            , HttpServletResponse response) throws IOException {
        if (password != null && !password.isEmpty() && password.equals(verifyPassword)) {
            Session session = securityService.register(login, password);
            if (session != null) {
                Cookie cookie = WebUtil.createCookieFromToken(session.getToken(), cookieMaxAge);
                response.addCookie(cookie);
                return "redirect:/";
            } else {
                return "redirect:/register";
            }
        } else {
            return "redirect:/register";
        }
    }


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String getLogin() {
        HashMap<String, Object> parameters = new HashMap<>();
        String page = pageGenerator.getPage("login", parameters);
        return page;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String login,
                        @RequestParam String password,
                        HttpServletResponse response) throws IOException {
        Session session = securityService.login(login, password);
        if (session != null) {
            Cookie cookie = WebUtil.createCookieFromToken(session.getToken(), cookieMaxAge);
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            return "redirect:/register";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user-token")) {
                securityService.logout(cookie.getValue());
                cookie.setMaxAge(-1);
            }
        }
        response.sendRedirect("/login");
    }

    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Value("${web.sessionMaxDurationInSeconds}")
    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }
}
