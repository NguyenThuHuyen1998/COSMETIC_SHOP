package com.example.crud.service.impl;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.*;
import com.example.crud.helper.TimeHelper;
import com.example.crud.output.OrderLineForm;
import com.example.crud.predicate.PredicateOrderFilter;
import com.example.crud.repository.CartItemRepository;
import com.example.crud.repository.CartRepository;
import com.example.crud.repository.OrderLineRepository;
import com.example.crud.repository.OrderRepository;
import com.example.crud.response.OrderResponse;
import com.example.crud.service.CartItemService;
import com.example.crud.service.CartService;
import com.example.crud.service.OrderService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.example.crud.service.VoucherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/*
    created by HuyenNgTn on 15/11/2020
*/
@Service
public class OrderServiceImpl implements OrderService {
    public static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private VoucherService voucherService;
    private OrderLineRepository orderLineRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            CartItemService cartItemService,
                            VoucherService voucherService,
                            OrderLineRepository orderLineRepository) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.cartRepository = cartRepository;
        this.voucherService = voucherService;
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    public List<Order> findAllOrder() {
        return (List<Order>) orderRepository.findAll();
    }


    @Override
    public Order findById(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId).get();
//            List<OrderLine> orderLines= orderLineRepository.getListOrderLineInOrder(orderId);
//            List<OrderLineForm> orderLineForms= new ArrayList<>();
//            for(OrderLine orderLine: orderLines){
//                OrderLineForm orderLineForm= new OrderLineForm(orderLine);
//                orderLineForms.add(orderLineForm);
//            }
//            OrderForm orderForm= new OrderForm(order, orderLineForms);
            return order;
        } catch (Exception e) {
            logger.error(String.valueOf(e));
            return null;
        }
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).get();
    }

    @Override
    public List<Order> getListOrderByStatus(String status, long userId) {
        List<Order> listAll= findAllOrder();
        List<Order> result= new ArrayList<>();
        for (Order order: listAll){
            if (order.getStatus().equals(status) && order.getUser().getUserId()== userId){
                result.add(order);
            }
        }
        return result;
    }

    @Override
    public List<Long> getlistProductBought(long userId){
        List<Long> productIds= new ArrayList<>();
        List<Order> orderList= getListOrderByStatus(InputParam.FINISHED, userId);
        if (orderList== null || orderList.size()==0){
            return null;
        }
        for (Order order: orderList){
            List<OrderLine> orderLineList= orderLineRepository.getListOrderLineInOrder(order.getOrderId());
            for (OrderLine orderLine: orderLineList){
                productIds.add(orderLine.getProduct().getId());
            }
        }
        return productIds.stream().distinct().collect(Collectors.toList());
    }
    @Override
    public List<Order> getListOrderByUserId(long userId) {
        return orderRepository.getListOrderByUserId(userId);
    }

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void remove(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public List<Order> filterOrder(Map<String, Object> filter) throws ParseException {
        long userId = (long) filter.get(InputParam.USER_ID);
        String status = (String) filter.get(InputParam.STATUS);
        String dateStart = (String) filter.get(InputParam.TIME_START);
        String dateEnd = (String) filter.get(InputParam.TIME_END);
        double priceMin = (double) filter.get(InputParam.PRICE_MIN);
        double priceMax = (double) filter.get(InputParam.PRICE_MAX);
        String sortBy = (String) filter.get(InputParam.SORT_BY);

        Predicate<Order> predicate = null;
        PredicateOrderFilter predicateOrderFilter = PredicateOrderFilter.getInstance();
        Predicate<Order> checkStatus = predicateOrderFilter.checkStatus(status);
        Predicate<Order> checkPrice = predicateOrderFilter.checkPrice(priceMin, priceMax);
        Predicate<Order> checkDate = predicateOrderFilter.checkDate(dateStart, dateEnd);
        Predicate<Order> checkUser = predicateOrderFilter.checkUser(userId);
        predicate = checkPrice.and(checkDate).and(checkUser).and(checkStatus);
        List<Order> orderList = predicateOrderFilter.filterOrder(findAllOrder(), predicate);
        orderList= sortByDateSell(orderList, sortBy);
        return orderList;

    }

    @Override
    public OrderResponse showOrder(User user) {
        long userId = user.getUserId();
        Order order = new Order(user, InputParam.PROCESSING, new Date().getTime());
        Cart cart = cartRepository.getCartByUserId(userId);
        List<CartItem> cartItemList = cartItemService.getListCartItemInCart(userId);
        List<OrderLineForm> orderLineForms = new ArrayList<>();
        if (cartItemList != null && cartItemList.size() > 0) {
            order = new Order(user, InputParam.PROCESSING, new Date().getTime());
            order.setTotal(cart.getTotalMoney());
            order.setRealPay(cart.getTotalMoney());
            for (CartItem cartItem : cartItemList) {
                OrderLine orderLine = new OrderLine(cartItem, order);
                OrderLineForm orderLineForm = new OrderLineForm(orderLine);
                orderLineForms.add(orderLineForm);
            }
            order.setVoucher(0);
        }
        OrderResponse orderResponse = new OrderResponse(order, orderLineForms);
        return orderResponse;
    }

    @Override
    public OrderResponse showOrder(User user, Voucher voucher) {
        long userId = user.getUserId();
        Order order = new Order(user, InputParam.PROCESSING, new Date().getTime());
        Cart cart = cartRepository.getCartByUserId(userId);
        List<CartItem> cartItemList = cartItemService.getListCartItemInCart(userId);
        List<OrderLineForm> orderLineForms = new ArrayList<>();
        if (cartItemList != null && cartItemList.size() > 0) {
            order = new Order(user, InputParam.PROCESSING, new Date().getTime());
            order.setTotal(cart.getTotalMoney());
            for (CartItem cartItem : cartItemList) {
                OrderLine orderLine = new OrderLine(cartItem, order);
                OrderLineForm orderLineForm = new OrderLineForm(orderLine);
                orderLineForms.add(orderLineForm);
            }
            if (!voucherService.validateVoucher(order, voucher)) {
                return null;
            }
//            order.setVoucher(voucher);
        }
        OrderResponse orderResponse = new OrderResponse(order, orderLineForms, voucher);
        return orderResponse;
    }

    @Override
    public OrderResponse createOrder(User user, String note, String delivery, Voucher voucher, Address address) {
        long userId = user.getUserId();
        Order order = new Order(user, InputParam.PROCESSING, new Date().getTime());
        order.setNote(note);
        order.setDelivery(delivery);
        order.setAddress(address);
        orderRepository.save(order);
        Cart cart = cartRepository.getCartByUserId(userId);
        List<CartItem> cartItemList = cartItemService.getListCartItemInCart(userId);
        List<OrderLineForm> orderLineForms = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            OrderLine orderLine = new OrderLine(cartItem, order);
            orderLineRepository.save(orderLine);
            cartItemService.deleteCartItem(cartItem);
            OrderLineForm orderLineForm = new OrderLineForm(orderLine);
            orderLineForms.add(orderLineForm);
        }
        order.setTotal(cart.getTotalMoney());
        cart.setTotalMoney(0);
        cartRepository.save(cart);

        if (voucher != null && voucherService.validateVoucher(order, voucher)) {
            applyVoucher(order, voucher);
        }
        order.setRealPay(order.getTotal()- order.getVoucher());
        orderRepository.save(order);
        OrderResponse orderResponse= new OrderResponse(order, orderLineForms);
        return orderResponse;
    }

    @Override
    public Map<Long, Integer> getListProductBestSeller() throws ParseException {
        String dateStart= TimeHelper.getInstance().getFirstInMonth();
        String dateEnd= TimeHelper.getInstance().getLastDayInMonth();
        Predicate<Order> predicate= null;
        PredicateOrderFilter orderFilter = PredicateOrderFilter.getInstance();
        Predicate<Order> checkDateOrder = orderFilter.checkDate(dateStart, dateEnd);
        predicate = checkDateOrder;
        PredicateOrderFilter predicateOrderFilter = PredicateOrderFilter.getInstance();
        List<Order> orderList = predicateOrderFilter.filterOrder(findAllOrder(), predicate);
        Map<Long, Integer> reportProduct= new HashMap<>();
        for (Order order: orderList){
            List<OrderLine> orderLines= orderLineRepository.getListOrderLineInOrder(order.getOrderId());
            for (OrderLine orderLine: orderLines){
                long productId= orderLine.getProduct().getId();
                if (reportProduct.containsKey(orderLine.getProduct().getId())) {
                    int quantity= reportProduct.get(productId);
                    reportProduct.put(productId, quantity+ orderLine.getAmount());
                }
                else reportProduct.put(productId, orderLine.getAmount());
            }
        }
        return reportProduct;
    }


    public void applyVoucher(Order order, Voucher voucher) {
        double value= voucher.getValueDiscount();
        if (voucher.getTypeDiscount().equals(InputParam.PERCENT)){
            order.setVoucher(value* order.getTotal()/100);
        }
        else order.setVoucher(value);
        User user= order.getUser();
        UserVoucher userVoucher= new UserVoucher(user, voucher);
        voucherService.addUserVoucher(userVoucher);
    }

    public List<Order> sortByDateSell(List<Order> orders, String sortBy) {
        if(sortBy.equals(InputParam.INCREASE)){
            Collections.sort(orders, new Comparator<Order>() {
                public int compare(Order o1, Order o2) {
                    return o1.getTime() > o2.getTime() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        if (sortBy.equals(InputParam.DECREASE)){
            Collections.sort(orders, new Comparator<Order>() {
                public int compare(Order o1, Order o2) {
                    return o1.getTime() < o2.getTime() ? 1 : (o1 == o2 ? 0 : -1);
                }
            });
        }
        return orders;
    }



    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse("22/12/2020");
        System.out.println(date.getTime());
    }
}
