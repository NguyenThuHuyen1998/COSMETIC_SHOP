package com.example.crud.service.impl;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.Category;
import com.example.crud.entity.Product;
import com.example.crud.predicate.PredicateProductFilter;
import com.example.crud.repository.CategoryRepository;
import com.example.crud.repository.ProductRepository;
import com.example.crud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.apache.commons.collections4.ListUtils;

import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository= productRepository;
    }


    @Override
    public List<Product> findAllProduct() {
        return (List<Product>) productRepository.findAll();
    }

    @Override
    public Product findById(Long productId) {
        Optional<Product> optionalProduct= productRepository.findById(productId);
        return optionalProduct.get();
    }
//
    @Override
    public List<Product> findByCategoryID(Long categoryId) {
        return productRepository.findProductByCategoryId(categoryId);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void remove(Product product) {
        productRepository.delete(product);
    }


    @Override
    public List<Product> filterProduct(Map<String, Object> filter) throws ParseException {
        double priceMin= (double) filter.get(InputParam.PRICE_MIN);
        double priceMax= (double) filter.get(InputParam.PRICE_MAX);
        long categoryId= (long) filter.get(InputParam.CATEGORY_ID);
        String keyword= (String) filter.get(InputParam.KEY_WORD);
        String sortBy= (String) filter.get(InputParam.SORT_BY);

        Predicate<Product> predicate= null;
        PredicateProductFilter productFilter = PredicateProductFilter.getInstance();
        Predicate<Product> checkPrice = productFilter.checkPrice(priceMin, priceMax);
        Predicate<Product> checkKeyword = productFilter.checkKeyword(keyword);
        Predicate<Product> checkCategory= productFilter.checkCategory(categoryId);
        Predicate<Product> checkActive= productFilter.checkActive();
        predicate = checkPrice.and(checkKeyword).and(checkCategory).and(checkActive);
        List<Product> productList= filterProduct(findAllProduct(), predicate);
        productList= sortBy(productList, sortBy);
        return productList;
    }


    public List<Product> sortBy(List<Product> products, String sortBy){
        if(sortBy.equals(InputParam.PRICE_INCREASE)){
            Collections.sort(products, new Comparator<Product>() {
                public int compare(Product o1, Product o2) {
                    return o1.getPrice() > o2.getPrice() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        if (sortBy.equals(InputParam.PRICE_DECREASE)){
            Collections.sort(products, new Comparator<Product>() {
                public int compare(Product o1, Product o2) {
                    return o1.getPrice() < o2.getPrice() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        if (sortBy.equals(InputParam.NEWEST)){
            Collections.sort(products, new Comparator<Product>() {
                public int compare(Product o1, Product o2) {
                    return o1.getDate() < o2.getDate() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        return products;
    }
    public static List<Product> filterProduct (List<Product> products,
                                                 Predicate<Product> predicate)
    {
        return products.stream()
                .filter( predicate )
                .collect(Collectors.<Product>toList());
    }
}
