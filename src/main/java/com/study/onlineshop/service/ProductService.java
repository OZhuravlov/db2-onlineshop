package com.study.onlineshop.service;

import com.study.onlineshop.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    Product getById(int id);

    void add(Product product);

    void delete(int id);

    void update(int id, String name, double price);

}
