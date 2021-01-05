package com.example.crud.service;

import com.example.crud.entity.Cart;
import com.example.crud.entity.CartItem;
import com.example.crud.entity.Product;

import java.util.List;
import java.util.Optional;

/*
    created by HuyenNgTn on 22/11/2020
*/
public interface CartService {
     void save(Cart cart);
     Cart getCartById(long cartId);
     Cart getCartByUserId(long userId);
     List<CartItem> getlistCartItem(long objectId);
     void deleteAllCartItem(long userId);
}
