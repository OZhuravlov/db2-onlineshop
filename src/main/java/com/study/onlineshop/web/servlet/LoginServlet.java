package com.study.onlineshop.web.servlet;

import com.study.onlineshop.security.SecurityService;
import com.study.onlineshop.security.Session;
import com.study.onlineshop.web.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class LoginServlet extends HttpServlet {
    private SecurityService securityService;

    public LoginServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PageGenerator pageGenerator = PageGenerator.instance();
        HashMap<String, Object> parameters = new HashMap<>();

        String page = pageGenerator.getPage("login", parameters);
        response.getWriter().write(page);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        System.out.println(login + " : " + password);

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
}

// id, login, password, role

// UI -> (login, password) Server -> query DB

// (trubintolik@gmail.com, 12345)

// register -> login + password -> save to db login + sha1(password)
// login -> login + password -> login + sha1(password) -> query from db

// sole = 'db2_onlineshop'
// register -> login + password -> save to db login + sha1(password + sole)
// login -> login + password -> login + sha1(password + sole) -> query from db


// sole = random -> UUID.randomUUID().toString()
// user (id, login, password, sole, userRole)
// register -> login + password -> save to db login + sha1(password + sole)
// login -> login + password -> login + sha1(password + sole) -> query from db
