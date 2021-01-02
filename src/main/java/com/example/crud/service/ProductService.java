package com.example.crud.service;

import com.example.crud.entity.Product;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProduct();
    Product findById(Long productId);
    List<Product> findByCategoryID(Long categoryId);
    List<Product> filterProduct(Map<String, Object> input) throws ParseException;
    void save (Product product);
    void remove (Product product);
//    Product update (Product product);
}
