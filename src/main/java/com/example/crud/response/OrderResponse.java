package com.example.crud.response;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.Address;
import com.example.crud.entity.Order;
import com.example.crud.entity.Voucher;
import com.example.crud.helper.TimeHelper;
import com.example.crud.output.OrderLineForm;
import com.example.crud.service.OrderService;
import com.example.crud.service.impl.OrderServiceImpl;

import java.io.Serializable;
import java.util.List;

/*
    created by HuyenNgTn on 03/12/2020
*/
public class OrderResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private long orderId;
    private long userId;
    private String time;
    private String status;
    private double total;
    private double realPay;
    private double voucher;
    private Address address;
    private List<OrderLineForm> orderLineList;

    public OrderResponse(){

    }

    public OrderResponse(Order order, List<OrderLineForm> orderLineForms, Voucher voucher){
        this.orderId= order.getOrderId();
        this.userId= order.getUser().getUserId();
        this.status= order.getStatus();
        this.total= order.getTotal();
        this.address= order.getAddress();
        this.time= TimeHelper.getInstance().getDate(order.getTime());
        if(voucher== null){
            this.voucher= 0;
        }
        else {
            if (voucher.getTypeDiscount().equals(InputParam.PERCENT)){
                double value= voucher.getValueDiscount();
                this.voucher= value* order.getTotal()/100;
            }
            else {
                this.voucher= voucher.getValueDiscount();
            }
        }

        this.realPay= order.getTotal()- this.voucher;
        this.orderLineList= orderLineForms;
    }

    public OrderResponse(Order order, List<OrderLineForm> orderLineForms){
        this.orderId= order.getOrderId();
        this.userId= order.getUser().getUserId();
        this.status= order.getStatus();
        this.total= order.getTotal();
        this.address= order.getAddress();
        this.time= TimeHelper.getInstance().getDate(order.getTime());
        double counpon= 0;
        Voucher voucher= order.getVoucher();
        if (voucher!= null){
            double value= voucher.getValueDiscount();
            if (voucher.getTypeDiscount().equals(InputParam.PERCENT)){
                counpon= value* order.getTotal()/100;
            }
            else counpon= value;
        }
        this.voucher= counpon;
        this.realPay= order.getRealPay();
        this.orderLineList= orderLineForms;
    }
//    public OrderResponse(long orderId, long userId, String time, String status, double total, double voucher, double realPay, Address address, List<OrderLineForm> orderLineList){
//        this.orderId= orderId;
//        this.userId= userId;
//        this.time= time;
//        this.status= status;
//        this.voucher= voucher;
//        this.total= total;
//        this.realPay= realPay;
//        this.address= address;
//        this.orderLineList= orderLineList;
//    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public double getVoucher() {
        return voucher;
    }

    public void setVoucher(double voucher) {
        this.voucher = voucher;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderLineForm> getOrderLineList() {
        return orderLineList;
    }

    public void setOrderLineList(List<OrderLineForm> orderLineList) {
        this.orderLineList = orderLineList;
    }


}
