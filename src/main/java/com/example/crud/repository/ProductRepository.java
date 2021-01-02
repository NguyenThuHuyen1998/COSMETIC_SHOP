package com.example.crud.repository;

import com.example.crud.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
    @Query("select t from Product t left join fetch t.category tcc where tcc.id = :id")
    List<Product> findProductByCategoryId(@Param("id") long cateId);

//    @Query("select t from Product t where t.price <=: priceMax and t.price>=: priceMin and t.name like '%:keyword+%' ")
//    List<Product> findProductByPriceKeyword(@Param("priceMax") double priceMax, @Param("priceMin") double priceMin, @Param("keyword") String keyword);

//    @Query("select t from Product where t.name like concat('%',:keyword,'%') or t")
//    List<Product> findProductByKeyword(@Param("keyword") String keyword);

}
