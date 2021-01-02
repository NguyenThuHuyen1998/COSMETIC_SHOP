package com.example.crud.response;

import com.example.crud.entity.Cart;
import com.example.crud.response.CartItemResponse;

import java.io.Serializable;
import java.util.List;

public class CartResponse implements Serializable {
    private long cartId;
    private List<CartItemResponse> cartItemResponses;
    private double totalMoney;
    private long userId;

    public CartResponse(Cart cart, List<CartItemResponse> cartItemResponses){
        this.cartId= cart.getCartId();
        this.cartItemResponses = cartItemResponses;
        this.totalMoney= cart.getTotalMoney();
        this.userId= cart.getUser().getUserId();
    }
    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public List<CartItemResponse> getCartItemForms() {
        return cartItemResponses;
    }

    public void setCartItems(List<CartItemResponse> cartItemResponses) {
        this.cartItemResponses = cartItemResponses;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
