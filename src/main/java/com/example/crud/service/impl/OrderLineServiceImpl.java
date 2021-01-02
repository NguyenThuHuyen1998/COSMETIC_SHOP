package com.example.crud.service.impl;

import com.example.crud.entity.OrderLine;
import com.example.crud.output.OrderLineForm;
import com.example.crud.repository.OrderLineRepository;
import com.example.crud.service.OrderLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    created by HuyenNgTn on 18/11/2020
*/
@Service
public class OrderLineServiceImpl implements OrderLineService {
    public static final Logger logger = LoggerFactory.getLogger(OrderLineServiceImpl.class);

    private OrderLineRepository orderLineRepository;

    @Autowired
    public OrderLineServiceImpl(OrderLineRepository orderLineRepository){
        this.orderLineRepository= orderLineRepository;
    }

    @Override
    public List<OrderLine> findAllOrderLine() {
        return (List<OrderLine>) orderLineRepository.findAll();

    }

    @Override
    public OrderLine findById(Long orderId) {
        Optional<OrderLine> optionalOrderLine= orderLineRepository.findById(orderId);
        return optionalOrderLine.get();
    }

    @Override
    public void save(OrderLine orderLine) {
        orderLineRepository.save(orderLine);
    }

    @Override
    public void remove(OrderLine orderLine) {
        orderLineRepository.delete(orderLine);
    }

    @Override
    public List<OrderLine> getListOrderLineInOrder(Long orderId) {
        return orderLineRepository.getListOrderLineInOrder(orderId);
    }

    @Override
    public List<OrderLineForm> getListOrderLineForm(List<OrderLine> orderLineList) {
        List<OrderLineForm> orderLineForms= new ArrayList<>();
        for (OrderLine orderLine: orderLineList){
            orderLineForms.add(new OrderLineForm(orderLine));
        }
        return orderLineForms;
//        return orderLineList.stream().filter(Objects::isNull).map(o ->{
//            OrderLineForm orderLineForm= new OrderLineForm();
//            orderLineForm.setOrderLineId(o.getOrderLineId());
//            orderLineForm.setProductId(o.getProduct() == null? 0: o.getProduct().getId());
//            orderLineForm.setProductName(o.getProduct() == null? null: o.getProduct().getName());
//            orderLineForm.setQuantity(o.getAmount());
//            return orderLineForm;
//        }).collect(Collectors.toList());
    }

}
