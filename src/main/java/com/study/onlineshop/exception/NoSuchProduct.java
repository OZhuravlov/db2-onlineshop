package com.study.onlineshop.exception;

public class NoSuchProduct extends RuntimeException {
    public NoSuchProduct(String message) {
        super(message);
    }
}
