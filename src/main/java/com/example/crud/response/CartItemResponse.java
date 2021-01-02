package com.example.crud.response;

import com.example.crud.entity.CartItem;
import com.example.crud.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/*
    created by HuyenNgTn on 11/12/2020
*/
public class CartItemResponse implements Serializable {
    public static final Logger logger = LoggerFactory.getLogger(CartItemResponse.class);

    private long cartItemId;
    private Product product;
    private long userId;
    private int quantity;

    public CartItemResponse(long cartItemId, Product product, long userId, int quantity){
        this.cartItemId= cartItemId;
        this.product= product;
        this.userId= userId;
        this.quantity= quantity;
    }

    public CartItemResponse(CartItem cartItem){
        this.cartItemId= cartItem.getCartItemId();
        this.product= cartItem.getProduct();
        this.userId= cartItem.getCart().getUser().getUserId();
        this.quantity= cartItem.getQuantity();
    }

    public long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
