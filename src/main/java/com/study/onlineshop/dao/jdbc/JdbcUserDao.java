package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.UserDao;
import com.study.onlineshop.dao.jdbc.mapper.UserRowMapper;
import com.study.onlineshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class JdbcUserDao implements UserDao {

    private static final String GET_SQL = "SELECT id, login, user_role, encrypted_password, sole FROM users WHERE login = ?";
    private static final String ADD_SQL = "INSERT INTO users(login, user_role, encrypted_password, sole) VALUES (?, ?, ?, ?);";
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();

    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUser(String login) {
        return jdbcTemplate.queryForObject(GET_SQL
                , new Object[]{login}, USER_ROW_MAPPER);
    }

    @Override
    public int add(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(ADD_SQL);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getUserRole().toString());
            ps.setString(3, user.getEncryptedPassword());
            ps.setString(4, user.getSole());
            return ps;
        }, keyHolder);

        return (int) keyHolder.getKey();
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
