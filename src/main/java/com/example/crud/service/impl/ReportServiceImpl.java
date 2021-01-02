package com.example.crud.service.impl;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.Order;
import com.example.crud.entity.OrderLine;
import com.example.crud.entity.Product;
import com.example.crud.helper.TimeHelper;
import com.example.crud.repository.ProductRepository;
import com.example.crud.response.ReportProductResponse;
import com.example.crud.predicate.PredicateOrderFilter;
import com.example.crud.repository.OrderLineRepository;
import com.example.crud.repository.OrderRepository;
import com.example.crud.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.LongStream;

@Service
public class ReportServiceImpl implements ReportService {
    private OrderRepository orderRepository;
    private OrderLineRepository orderLineRepository;
    private ProductRepository productRepository;
    private Map<String, Object> reportRevenue;
    private Map<String, Long> reportProduct;
    private Map<String, Object> report;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    TimeHelper timeHelper= new TimeHelper();

    @Autowired
    public ReportServiceImpl(OrderRepository orderRepository, OrderLineRepository orderLineRepository, ProductRepository productRepository){
        this.orderRepository= orderRepository;
        this.orderLineRepository= orderLineRepository;
        this.productRepository= productRepository;
    }

    public List<Order> filterOrder(String dateStart, String dateEnd) throws ParseException {
        Predicate<Order> predicate= null;
        PredicateOrderFilter predicateOrderFilter= PredicateOrderFilter.getInstance();
        Predicate<Order> checkDate= predicateOrderFilter.checkDate(dateStart, dateEnd);
        predicate= checkDate;
        List<Order> totalOrder= (List<Order>) orderRepository.findAll();
        List<Order> orderList=predicateOrderFilter.filterOrder(totalOrder, predicate);
        return orderList;
    }

    @Override
    public Map<String, Object> getReport() throws ParseException{
        report= new HashMap<>();
        report.put(InputParam.TODAY, getReportByDay());
        report.put(InputParam.THIS_WEEK, getReportByWeek());
        report.put(InputParam.THIS_MONTH, getReportByMonth());
        report.put(InputParam.THIS_YEAR, getReportByYear());
        return report;
    }

    public Map<String, Long> getReportByDay() throws ParseException {
        String start= LocalDate.now().format(formatter);
        String end= LocalDate.now().format(formatter);
        List<Order> orderList= filterOrder(start, end);
        Map<String, Long> reportToday= getReportProduct(orderList);
        long count= 0;
        for(Map.Entry<String, Long> entry : reportToday.entrySet()) {
            if(entry.getKey().equals("total")) continue;
            count= count+ entry.getValue();
        }
        reportToday.put("totalCount", count);
        return reportToday;
    }

    public Map<String, Long> getReportByWeek() throws ParseException {
        String start= timeHelper.getFirstDayInWeek();
        String end= timeHelper.getLastDayInWeek();
        List<Order> orderList= filterOrder(start, end);
        Map<String, Long> reportThisWeek= getReportProduct(orderList);
        long count= 0;
        for(Map.Entry<String, Long> entry : reportThisWeek.entrySet()) {
            if(entry.getKey().equals("total")) continue;
            count= count+ entry.getValue();
        }
        reportThisWeek.put("totalCount", count);
        return reportThisWeek;
    }

    public Map<String, Long> getReportByMonth() throws ParseException {
        String start= timeHelper.getFirstInMonth();
        String end= timeHelper.getLastDayInMonth();
        List<Order> orderList= filterOrder(start, end);
        Map<String, Long> reportThisMonth= getReportProduct(orderList);
        long count= 0;
        for(Map.Entry<String, Long> entry : reportThisMonth.entrySet()) {
            if(entry.getKey().equals("total")) continue;
            count= count+ entry.getValue();
        }
        reportThisMonth.put("totalCount", count);
        return reportThisMonth;
    }

    public Map<String, Long> getReportByYear() throws ParseException {
        String start= timeHelper.getFirstDayOfYear();
        String end= timeHelper.getLastDayOfYear();
        List<Order> orderList= filterOrder(start, end);
        Map<String, Long> reportThisYear= getReportProduct(orderList);
        long count= 0;
        for(Map.Entry<String, Long> entry : reportThisYear.entrySet()) {
            if(entry.getKey().equals("total")) continue;
            count= count+ entry.getValue();
        }
        reportThisYear.put("totalCount", count);
        return reportThisYear;
    }


    public Map<String, Long> getReportProduct(List<Order> orders){
        try{
            reportProduct= new HashMap<>();
            double totalRevenue= 0;
            if(orders.size()>0){
                for(Order order: orders){
                    if (order.getStatus().equals(InputParam.PROCESSING) ) continue;
                    List<OrderLine> orderLines= orderLineRepository.getListOrderLineInOrder(order.getOrderId());
                    for (OrderLine orderLine: orderLines){
                        long productId= orderLine.getProduct().getId();
                        String productName= productRepository.findById(productId).get().getName();
                        if(reportProduct.containsKey(productId)){
                            long count= reportProduct.get(productId);
                            reportProduct.put(productName, count+orderLine.getAmount());
                        }
                        else {
                            reportProduct.put(productName, Long.valueOf(orderLine.getAmount()));
                        }
                    }
                    totalRevenue= totalRevenue+ order.getRealPay();
                }
            }
            reportProduct.put("total", (long) totalRevenue);
            return reportProduct;
        }
        catch (Exception e){
            return null;
        }
    }

    public List<Product> getListProductBestSeller(int limit) throws ParseException {
//        // get list best seller product in this month
//        String start= timeHelper.getFirstInMonth();
//        String end= timeHelper.getLastDayInMonth();
//        List<Order> orderList= filterOrder(start, end);
//        for(Order order: orderList){
//            if (order.getStatus().equals(InputParam.PROCESSING) ) continue;
//            List<OrderLine> orderLines= orderLineRepository.getListOrderLineInOrder(order.getOrderId());
//            for (OrderLine orderLine: orderLines){
//                long productId= orderLine.getProduct().getId();
//                String productName= productRepository.findById(productId).get().getName();
//                if(reportProduct.containsKey(productId)){
//                    long count= reportProduct.get(productId);
//                    reportProduct.put(productName, count+orderLine.getAmount());
//                }
//                else {
//                    reportProduct.put(productName, Long.valueOf(orderLine.getAmount()));
//                }
//            }
//            totalRevenue= totalRevenue+ order.getTotalPrice();
//        }
//        Map<String, Long> reportByMonth = getReportByMonth();
//        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
//
//        reportByMonth.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
//        List<Long> productIds= new ArrayList<>();
//        reportByMonth.entrySet().stream()
//                .limit(5)
//                .forEach(e -> productIds.add());
        return null;
    }

}

