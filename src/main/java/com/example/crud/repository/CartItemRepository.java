package com.example.crud.repository;

import com.example.crud.entity.CartItem;
import com.example.crud.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
    created by HuyenNgTn on 22/11/2020
*/
@Repository
public interface CartItemRepository extends PagingAndSortingRepository<CartItem, Long> {
    @Query("select t from CartItem t where t.product.id = :id and t.cart.user.userId= :userId")
    CartItem findCartItemByProductId(@Param("id") long productId, @Param("userId") long userId);

    @Query("select t from CartItem t join t.cart tcc join tcc.user tcu where tcu.userId =:id")
    List<CartItem> getListCartItemInCart(@Param("id") long userId);
}
