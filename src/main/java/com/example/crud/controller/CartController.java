package com.example.crud.controller;

import com.example.crud.entity.*;
import com.example.crud.response.CartResponse;
import com.example.crud.response.CartItemResponse;
import com.example.crud.service.CartItemService;
import com.example.crud.service.CartService;
import com.example.crud.service.JwtService;
import com.example.crud.service.VoucherService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/*
    created by HuyenNgTn on 23/11/2020
*/
@RestController
public class CartController {
    public static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private CartService cartService;

    private JwtService jwtService;

    private CartItemService cartItemService;

    @Autowired
    public CartController(CartService cartService, JwtService jwtService, CartItemService cartItemService){
        this.cartService= cartService;
        this.jwtService= jwtService;
        this.cartItemService= cartItemService;
    }

    @GetMapping(value = "/userPage/cart")
    public ResponseEntity<Cart> getACart(HttpServletRequest request){
        try{
            if(jwtService.isCustomer(request)){
                long userId= jwtService.getCurrentUser(request).getUserId();
                Cart cart= cartService.getCartByUserId(userId);
                List<CartItem> cartItemList= cartItemService.getListCartItemInCart(userId);

                // cartItemResponse la form tra ve danh sach san pham trong cart
                List<CartItemResponse> cartItemResponses = new ArrayList<>();
                for (CartItem cartItem: cartItemList){
                    cartItemResponses.add(new CartItemResponse(cartItem));
                }
                CartResponse cartResponse = new CartResponse(cart, cartItemResponses);
                return new ResponseEntity(cartResponse, HttpStatus.OK);
            }
            return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch (Exception e){
            logger.error(String.valueOf(e));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping(value = "/userPage/cart")
    public ResponseEntity<Cart> deleteAllCart(HttpServletRequest request){
        try{
            if(jwtService.isCustomer(request)){
                long userId= jwtService.getCurrentUser(request).getUserId();
                cartItemService.deleteAllCartItem(userId);
                return new ResponseEntity("Xóa giỏ hàng thành công!",HttpStatus.OK);
            }
            return new ResponseEntity("Đăng nhập trước khi thực hiện", HttpStatus.METHOD_NOT_ALLOWED);
        }
        catch (Exception e){
            logger.error(String.valueOf(e));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
