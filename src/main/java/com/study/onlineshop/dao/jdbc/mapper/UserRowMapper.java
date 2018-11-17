package com.study.onlineshop.dao.jdbc.mapper;

import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    // id, login, encrypted_password, salt, price
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setUserRole(UserRole.valueOf(rs.getString("user_role")));
        user.setEncryptedPassword(rs.getString("encrypted_password"));
        user.setSole(rs.getString("sole"));
        return user;
    }
}
