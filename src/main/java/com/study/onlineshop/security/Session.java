package com.study.onlineshop.security;

import com.study.onlineshop.entity.Cart;
import com.study.onlineshop.entity.User;

import java.time.LocalDateTime;

public class Session {
    private String token;
    private User user;
    private LocalDateTime expireDate;

    private Cart cart;

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

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

}
