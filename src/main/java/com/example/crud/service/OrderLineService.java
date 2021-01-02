package com.example.crud.service;

import com.example.crud.entity.OrderLine;
import com.example.crud.output.OrderLineForm;

import java.util.List;

public interface OrderLineService {
    List<OrderLine> findAllOrderLine();
    OrderLine findById(Long orderId);
    void save (OrderLine orderLine);
    void remove (OrderLine orderLine);
    List<OrderLine> getListOrderLineInOrder(Long orderId);
    List<OrderLineForm> getListOrderLineForm(List<OrderLine> orderLineList);
}
