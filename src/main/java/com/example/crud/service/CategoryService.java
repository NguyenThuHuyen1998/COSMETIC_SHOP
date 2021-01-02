package com.example.crud.service;


import com.example.crud.entity.Category;
import com.example.crud.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAllCategory();
    Category findById(Long categoryId);
    List<Product> getListProductFromCategory(long categoryId);
    void save (Category category);
    void update(Category category);
    void remove (Category category);
}
;