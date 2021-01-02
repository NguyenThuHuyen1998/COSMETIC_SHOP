package com.example.crud.repository;

import com.example.crud.entity.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
    created by HuyenNgTn on 22/11/2020
*/
@Repository
public interface CartRepository extends PagingAndSortingRepository<Cart, Long> {
    public static final Logger logger = LoggerFactory.getLogger(CartRepository.class);

    @Query("select t from Cart t left join fetch t.user tcc where tcc.userId = :userId")
    Cart getCartByUserId(@Param("userId") long userId);

}