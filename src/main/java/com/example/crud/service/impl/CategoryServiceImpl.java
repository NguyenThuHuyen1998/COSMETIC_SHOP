package com.example.crud.service.impl;

import com.example.crud.entity.Category;
import com.example.crud.entity.Product;
import com.example.crud.repository.CategoryRepository;
import com.example.crud.repository.ProductRepository;
import com.example.crud.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository){
        this.categoryRepository= categoryRepository;
        this.productRepository= productRepository;
    }

    @Override
    public List<Category> findAllCategory() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Category findById(Long categoryId) {
        Optional<Category> optionalCategory= categoryRepository.findById(categoryId);
        return optionalCategory.get();
    }

    @Override
    public List<Product> getListProductFromCategory(long categoryId) {
        try{
            List<Product> productList= productRepository.findProductByCategoryId(categoryId);
            return productList;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void update(Category category) {
        long categoryId= category.getId();
        Category oldCategory= findById(categoryId);
        oldCategory.setDescription(category.getDescription()== null? oldCategory.getDescription()  : category.getDescription());
        oldCategory.setName(category.getName()==null ? oldCategory.getName(): category.getName());
        categoryRepository.save(category);
    }

    @Override
    public void remove(Category category) {
        categoryRepository.delete(category);
    }
}
