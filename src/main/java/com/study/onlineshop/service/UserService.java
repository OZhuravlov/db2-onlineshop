package com.study.onlineshop.service;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;

import java.util.List;

public interface UserService {

    User getUser(String login, String password);

    void add(String login, String password);

    void add(String login, String password, UserRole userRole);

}
