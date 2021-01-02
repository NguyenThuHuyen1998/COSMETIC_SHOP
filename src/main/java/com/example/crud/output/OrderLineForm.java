package com.example.crud.output;

import com.example.crud.entity.OrderLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/*
    created by HuyenNgTn on 03/12/2020
*/
public class OrderLineForm {
    public static final Logger logger = LoggerFactory.getLogger(OrderLineForm.class);
    private long orderLineId;
    private long productId;
    private String productName;
    private int quantity;
    private double money;

    public OrderLineForm() {
    }

    public OrderLineForm(long orderLineId, long productId, String productName, int quantity, double money) {
        this.orderLineId = orderLineId;
        this.productId = productId;
        this.productName= productName;
        this.quantity = quantity;
        this.money= money;
    }

    public OrderLineForm(OrderLine orderLine){
        this.orderLineId= orderLine.getOrderLineId();
        this.productId= orderLine.getProduct().getId();
        this.quantity= orderLine.getAmount();
        this.productName= orderLine.getProduct().getName();
        this.money= orderLine.getValueLine();
    }

    public List<OrderLineForm> change(List<OrderLine> orderLineList){
        List<OrderLineForm> orderLineForms= new ArrayList<>();
        for (OrderLine orderLine: orderLineList){
            orderLineForms.add(new OrderLineForm(orderLine));
        }
        return orderLineForms;
    }

    public long getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(long orderLineId) {
        this.orderLineId = orderLineId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
