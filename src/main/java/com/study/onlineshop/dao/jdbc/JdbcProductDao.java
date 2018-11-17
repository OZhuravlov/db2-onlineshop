package com.study.onlineshop.dao.jdbc;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.dao.jdbc.mapper.ProductRowMapper;
import com.study.onlineshop.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcProductDao implements ProductDao {

    private static final String GET_ALL_SQL = "SELECT id, name, creation_date, price FROM products";
    private static final String GET_SQL = "SELECT id, name, creation_date, price FROM products WHERE id = ?";
    private static final String ADD_SQL = "INSERT INTO products(name, price, creation_date) VALUES (?, ?, ?);";
    private static final String DELETE_SQL = "DELETE FROM products WHERE id = ?;";
    private static final String UPDATE_SQL = "UPDATE products SET name = ?, price = ? WHERE id = ?;";
    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Product> getAll() {
        return jdbcTemplate.query(GET_ALL_SQL, PRODUCT_ROW_MAPPER);
    }

    @Override
    public Product getById(int id) {
        return jdbcTemplate.queryForObject(GET_SQL, new Object[]{id}, PRODUCT_ROW_MAPPER);
    }

    @Override
    public void add(Product product) {
        jdbcTemplate.update(
                ADD_SQL, product.getName(), product.getPrice(), Timestamp.valueOf(product.getCreationDate())
        );
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update(UPDATE_SQL, product.getName(), product.getPrice(), product.getId());
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
