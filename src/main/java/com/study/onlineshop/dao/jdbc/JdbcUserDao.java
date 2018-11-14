package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.UserDao;
import com.study.onlineshop.dao.jdbc.mapper.UserRowMapper;
import com.study.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {

    private static final String GET_SQL = "SELECT id, login, user_role, encrypted_password, sole FROM users WHERE login = ?";
    private static final String ADD_SQL = "INSERT INTO users(login, user_role, encrypted_password, sole) VALUES (?, ?, ?, ?);";
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    @Autowired
    private DataSource dataSource;

    @Override
    public User getUser(String login) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_SQL)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return USER_ROW_MAPPER.mapRow(resultSet);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int add(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_SQL)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getUserRole().toString());
            statement.setString(3, user.getEncryptedPassword());
            statement.setString(4, user.getSole());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = 0;
            if (resultSet.next()) {
                id = statement.getGeneratedKeys().getInt(1);
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
