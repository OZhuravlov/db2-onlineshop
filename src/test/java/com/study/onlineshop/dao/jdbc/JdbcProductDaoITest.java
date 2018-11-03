package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.entity.Product;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class JdbcProductDaoITest {
    @Test
    public void testGetAll() throws Exception {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        connectionProvider.init();
        JdbcProductDao jdbcProductDao = new JdbcProductDao();
        jdbcProductDao.setConnectionProvider(connectionProvider);
        List<Product> products = jdbcProductDao.getAll();

        for (Product product : products) {
            assertNotNull(product.getName());
            assertNotNull(product.getCreationDate());
        }
    }

}