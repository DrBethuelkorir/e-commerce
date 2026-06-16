package com.beviamy.dreamers.exeption;

public class ProductNotFoundExeption extends RuntimeException{
    public ProductNotFoundExeption(String message) {
        super(message);
    }
}
