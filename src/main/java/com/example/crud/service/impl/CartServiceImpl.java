package com.example.crud.service.impl;

import com.example.crud.entity.Cart;
import com.example.crud.entity.CartItem;
import com.example.crud.entity.Product;
import com.example.crud.repository.CartItemRepository;
import com.example.crud.repository.CartRepository;
import com.example.crud.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
    created by HuyenNgTn on 22/11/2020
*/
@Service
public class CartServiceImpl implements CartService {
    public static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository){
        this.cartRepository= cartRepository;
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(long cartId) {
        Optional<Cart> optionalCart= cartRepository.findById(cartId);
        return optionalCart.get();
    }

    @Override
    public Cart getCartByUserId(long userId) {
        return cartRepository.getCartByUserId(userId);
    }

    @Override
    public List<CartItem> getlistCartItem(long userId) {
        Cart cart= cartRepository.getCartByUserId(userId);
        if(cart!= null){
            return cartItemRepository.getListCartItemInCart(userId);
        }
        return null;
    }

    @Override
    public void deleteAllCartItem(long userId) {
        List<CartItem> cartItems= cartItemRepository.getListCartItemInCart(userId);
        for (CartItem cartItem: cartItems){
            cartItemRepository.delete(cartItem);
        }
    }

}
