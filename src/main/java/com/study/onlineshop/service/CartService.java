package com.study.onlineshop.service;

import com.study.onlineshop.entity.Cart;

public interface CartService {

    void addToCart(Cart cart, int productId);

    void setProductService(ProductService productService);
}
