package com.study.onlineshop.service.impl;

import com.study.onlineshop.dao.ProductDao;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultProductService implements ProductService {

    private ProductDao productDao;

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product getById(int id) {
        return productDao.getById(id);
    }

    @Override
    public void add(Product product) {
        productDao.add(product);
    }

    @Override
    public void delete(int id) {
        productDao.delete(id);
    }

    @Override
    public void update(int id, String name, double price) {
        Product product = getById(id);
        product.setName(name);
        product.setPrice(price);
        productDao.update(product);
    }

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

}
