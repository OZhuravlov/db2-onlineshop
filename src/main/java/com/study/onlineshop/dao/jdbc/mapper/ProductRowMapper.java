package com.study.onlineshop.dao.jdbc.mapper;

import com.study.onlineshop.entity.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProductRowMapper implements RowMapper<Product> {

    // id, name, creationDate, price
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        Timestamp creationDate = rs.getTimestamp("creation_date");
        product.setCreationDate(creationDate.toLocalDateTime());
        return product;
    }

}
