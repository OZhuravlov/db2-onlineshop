package com.study.onlineshop.dao.jdbc.mapper;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {
    // id, login, encrypted_password, salt, price
    public User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("id"));
        user.setLogin(resultSet.getString("login"));
        user.setUserRole(UserRole.valueOf(resultSet.getString("user_role")));
        user.setEncryptedPassword(resultSet.getString("encrypted_password"));
        user.setSole(resultSet.getString("sole"));

        return user;
    }
}
