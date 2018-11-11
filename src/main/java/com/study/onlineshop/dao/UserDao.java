package com.study.onlineshop.dao;

import com.study.onlineshop.dao.jdbc.ConnectionProvider;
import com.study.onlineshop.entity.User;

public interface UserDao {

    User getUser(String login);

    int add(User product);

    void setConnectionProvider(ConnectionProvider connectionProvider);

}
