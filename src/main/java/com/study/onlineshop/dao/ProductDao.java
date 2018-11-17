package com.study.onlineshop.dao;

import com.study.onlineshop.entity.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getAll();

    Product getById(int id);

    void add(Product product);

    void delete(int id);

    void update(Product product);

}
