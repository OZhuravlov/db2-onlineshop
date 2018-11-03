package com.study.onlineshop.dao;

import com.study.onlineshop.entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {

    List<Product> getAll();

    Product getById(int id);

    int add(Product product);

    void delete(int id);

    void update(Product product);

}
