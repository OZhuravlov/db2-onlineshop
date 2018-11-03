package com.study.onlineshop.security;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class Session {
    private String token;
    private User user;
    private LocalDateTime expireDate;

    private List<Product> cart;

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addProductToCart(Product product){
        cart.add(product);
    }

    public List<Product> getCart() {
        return cart;
    }

}
