package com.example.crud.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

/*
    created by HuyenNgTn on 15/11/2020
*/

@Entity
@Table(name = "tblORDER_LINE")
public class OrderLine implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name= "order_line_id")
    private long orderLineId;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "amount")
    private int amount;

    @Column(name = "value_line")
    private double valueLine;


    public OrderLine() {
    }

    public OrderLine(long orderLineId, Product product, Order order, int amount) {
        this.orderLineId = orderLineId;
        this.order= order;
        this.product= product;
        this.amount = amount;
        this.valueLine= amount* product.getPrice();
    }

    public OrderLine(Product product, Order order, int amount) {
        this.order= order;
        this.product= product;
        this.amount = amount;
        this.valueLine= amount* product.getPrice();
    }

    public OrderLine(CartItem cartItem, Order order) {
        this.product= cartItem.getProduct();
        this.order= order;
        this.amount = cartItem.getQuantity();
        this.valueLine= cartItem.getQuantity()* cartItem.getProduct().getPrice();
    }
    public long getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(long orderLineId) {
        this.orderLineId = orderLineId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getValueLine() {
        return valueLine;
    }

//    public void setValueLine() {
//        this.valueLine = this.amount* this.getProduct().getPrice();
//    }

}
