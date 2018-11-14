package com.study.onlineshop.entity;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }
}
