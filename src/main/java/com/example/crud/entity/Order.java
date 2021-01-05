package com.example.crud.entity;


import javax.persistence.*;
import java.io.Serializable;

/*
    created by HuyenNgTn on 15/11/2020
*/
@Entity
@Table(name = "tblORDER")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "time")
    private long time;

    @Column(name = "total")
    private double total;

    @Column(name = "real_pay")
    private double realPay;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    @Column(name = "delivery")
    private String delivery;

    public Order(User user, String status, long time, String note, String delivery, Address address) {
        this.user = user;
        this.status = status;
        this.time= time;
        this.note= note;
        this.delivery= delivery;
        this.address= address;
    }
    public Order(User user, String status, long time) {
        this.user = user;
        this.status = status;
        this.time= time;
    }


    public Order(){

    }


    public Order(long orderId, User user, long time, double total, double realPay, String status, String note, Address address, Voucher voucher, String delivery) {
        this.orderId = orderId;
        this.user = user;
        this.time = time;
        this.total = total;
        this.realPay = realPay;
        this.status = status;
        this.note = note;
        this.address = address;
        this.voucher = voucher;
        this.delivery= delivery;
    }


    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getRealPay() {
        return realPay;
    }

    public void setRealPay(double realPay) {
        this.realPay = realPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
}
