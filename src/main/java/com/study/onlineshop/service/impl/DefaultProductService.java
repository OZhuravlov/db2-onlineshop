package com.study.onlineshop.service.impl;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;

import java.util.List;

public class DefaultProductService implements ProductService {
    private ProductDao productDao;

    public DefaultProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product getById(int id) {
        return productDao.getById(id);
    }

    @Override
    public int add(Product product) {
        int id = productDao.add(product);
        return id;
    }

    @Override
    public void delete(int id) {
        productDao.delete(id);
    }

    @Override
    public void update(int id, String name, double price) {
        Product product = getById(id);
        boolean isModified = !product.getName().equals(name) || product.getPrice() != price;
        if(isModified) {
            product.setName(name);
            product.setPrice(price);
            productDao.update(product);
        }
    }
}
