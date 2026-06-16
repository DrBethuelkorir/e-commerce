package com.beviamy.dreamers.exeption;

public class CategoryNotFoundExeption extends RuntimeException{
    public CategoryNotFoundExeption(String categoryNotFound) {
        super(categoryNotFound);
    }
}
