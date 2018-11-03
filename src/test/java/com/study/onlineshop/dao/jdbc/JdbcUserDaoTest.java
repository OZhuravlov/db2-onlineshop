package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.entity.User;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class JdbcUserDaoTest {
    @Test
    public void testGetAll() throws Exception {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        connectionProvider.init();
        JdbcUserDao jdbcUserDao = new JdbcUserDao();
        jdbcUserDao.setConnectionProvider(connectionProvider);
        List<User> products = jdbcUserDao.getAll();

        for (User user : products) {
            assertNotNull(user.getId());
            assertNotNull(user.getLogin());
            assertNotNull(user.getEncryptedPassword());
            assertNotNull(user.getSole());
            assertNotNull(user.getUserRole());
        }
    }

}
