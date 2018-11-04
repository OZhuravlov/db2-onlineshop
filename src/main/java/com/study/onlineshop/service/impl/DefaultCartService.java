package com.study.onlineshop.service.impl;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.Product;
import com.study.onlineshop.exception.NoSuchProduct;
import com.study.onlineshop.service.CartService;
import com.study.onlineshop.service.ProductService;

public class DefaultCartService implements CartService {
    ProductService productService;

    @Override
    public void addToCart(Cart cart, int productId) {
        Product product = productService.getById(productId);
        if (product == null) {
            throw new NoSuchProduct("No product with id = " + productId + " in catalog");
        }
        cart.getProducts().add(product);
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
