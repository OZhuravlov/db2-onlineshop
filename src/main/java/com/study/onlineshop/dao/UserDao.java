package com.study.onlineshop.dao;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.User;

import java.util.List;

public interface UserDao {

    List<User> getAll();

    User getUser(String login);

    int add(User product);

    void delete(String login);

    void update(User user);

}
