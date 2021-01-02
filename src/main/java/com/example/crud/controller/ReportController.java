package com.example.crud.controller;

import com.example.crud.constants.InputParam;
import com.example.crud.entity.Order;
import com.example.crud.entity.OrderLine;
import com.example.crud.helper.TimeHelper;
import com.example.crud.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

@RestController
public class ReportController {

    private OrderService orderService;
    private OrderLineService orderLineService;
    private UserService userService;
    private ReportService reportService;
    private JwtService jwtService;
    Map<Long, Integer> reportProduct= new HashMap<>();
    Map<Long, Double> reportRevenue= new HashMap<>();

    @Autowired
    public ReportController(OrderService orderService, UserService userService, ReportService reportService, JwtService jwtService, OrderLineService orderLineService){
        this.orderService= orderService;
        this.userService= userService;
        this.reportService= reportService;
        this.jwtService= jwtService;
        this.orderLineService= orderLineService;
    }

    @GetMapping(value = "/adminPage/report")
    public ResponseEntity<Order> reportProduct(HttpServletRequest request){
        if(jwtService.isAdmin(request)){
            try{
                //Map<String, Object> report= reportService.getReport();
                List<Order> orderList= orderService.findAllOrder();
                    return new ResponseEntity(orderList, HttpStatus.OK);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity("Bạn không phải là admin", HttpStatus.METHOD_NOT_ALLOWED);
    }

    public static void main(String[] args) {
        Calendar dateStart= Calendar.getInstance();
        DateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
//        for (int i=0; i<6; i++){
//            dateStart.add(Calendar.DATE, 1);
//        }
        System.out.println(dateFormat.format(dateStart.getTime()));
    }
}
