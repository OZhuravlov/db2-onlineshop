package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.entity.Product;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class JdbcProductDaoITest {
    @Test
    public void testGetAll() throws Exception {
        ConnectionProvider connectionProvider = new ConnectionProvider();
        ProductDao productDao = new JdbcProductDao();
        productDao.setConnectionProvider(connectionProvider);
        List<Product> products = productDao.getAll();

        for (Product product : products) {
            assertNotNull(product.getName());
            assertNotNull(product.getCreationDate());
        }
    }

}