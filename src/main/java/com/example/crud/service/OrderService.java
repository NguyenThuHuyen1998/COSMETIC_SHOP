package com.example.crud.service;

import com.example.crud.entity.*;
import com.example.crud.response.OrderResponse;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/*
    created by HuyenNgTn on 15/11/2020
*/
public interface OrderService {
    List<Order> findAllOrder();
    Order findById(Long orderId);
    Order getOrder(Long orderId);
    List<Order> getListOrderByStatus(String status, long userId);
    List<Long> getlistProductBought(long userId);
    List<Order> getListOrderByUserId(long userId);
    void save (Order order);
    void remove (Order order);
    List<Order> filterOrder(Map<String, Object> filter) throws ParseException;
    OrderResponse showOrder(User user);
    OrderResponse showOrder(User user, Voucher voucher);
    double applyVoucher(Order order, Voucher voucher);
    OrderResponse createOrder(User user, String note, String delivery, Voucher voucher, Address address);
    Map<Long, Integer> getListProductBestSeller() throws ParseException;
    OrderResponse createOrder(User user, String note, String delivery, Voucher voucher, Address address, Map<String, Integer> productList);
}
