package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.UserDao;
import com.study.onlineshop.dao.jdbc.mapper.UserRowMapper;
import com.study.onlineshop.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements UserDao {

    private static final String GET_ALL_SQL = "SELECT id, login, user_role, encrypted_password, sole FROM users";
    private static final String ADD_SQL = "INSERT INTO users(login, user_role, encrypted_password, sole) VALUES (?, ?, ?, ?);";
    private static final String DELETE_SQL = "DELETE FROM users WHERE login = ?;";
    private static final String UPDATE_SQL = "UPDATE users SET user_role = ?, encrypted_password = ?, sole = ? WHERE login = ?;";
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    private ConnectionProvider connectionProvider;

    @Override
    public List<User> getAll() {
        try (Connection connection = connectionProvider.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_SQL)) {


            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUser(String login) {
        String sql = GET_ALL_SQL + " WHERE login = ?;";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
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
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_SQL)){
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

    @Override
    public void delete(String login) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)){
            statement.setString(1, login);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)){
            statement.setString(1, user.getUserRole().toString());
            statement.setString(2, user.getEncryptedPassword());
            statement.setString(3, user.getSole());
            statement.setString(4, user.getLogin());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setConnectionProvider(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

}
