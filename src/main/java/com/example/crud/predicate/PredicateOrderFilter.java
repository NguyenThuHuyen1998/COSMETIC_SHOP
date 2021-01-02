package com.example.crud.predicate;

import com.example.crud.entity.Order;
import com.example.crud.helper.TimeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
    created by HuyenNgTn on 12/12/2020
*/
public class PredicateOrderFilter {
    public static final Logger logger = LoggerFactory.getLogger(PredicateOrderFilter.class);

    private static final PredicateOrderFilter predicateOrderFilter= new PredicateOrderFilter();

    private PredicateOrderFilter(){

    }

    public static PredicateOrderFilter getInstance(){
        return predicateOrderFilter;
    }

    public Predicate<Order> checkPrice(double minPrice, double maxPrice){
        return (order) ->{
            try{
                if(minPrice== -1){
                    if(maxPrice ==-1){
                        return true;
                    }
                    if(order.getRealPay() <= maxPrice){
                        return true;
                    }
                }
                else if(maxPrice== -1){
                    if (order.getRealPay() >= minPrice){
                        return true;
                    }
                }
                else if(order.getRealPay() >= minPrice && order.getRealPay() <= maxPrice){
                    return true;
                }
                return false;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        };

    }


    public Predicate<Order> checkDate(String dateStart, String dateEnd) throws ParseException {
        long timeStart= -1;
        long timeEnd= -1;

        if(!dateEnd.equals("-1") && !dateStart.equals("-1")){
            String start= dateStart+" 00:00:00";
            DateFormat formatter=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            timeStart=formatter.parse(start).getTime();

            String end= dateEnd+" 23:59:59";
            timeEnd=formatter.parse(end).getTime();
        }
        long finalTimeStart = timeStart;
        long finalTimeEnd = timeEnd;
        return (order) ->{
            try{
                if(finalTimeStart == -1){
                    if(finalTimeEnd ==-1){
                        return true;
                    }
                    if(order.getTime() <= finalTimeEnd){
                        return true;
                    }
                }
                else if(finalTimeEnd== -1){
                    if (order.getTime() >= finalTimeStart){
                        return true;
                    }
                }
                else if(order.getTime() >= finalTimeStart && order.getTime() <= finalTimeEnd){
                    return true;
                }
                return false;
            }
            catch (Exception e){
                e.printStackTrace();
                return false;
            }
        };

    }

    public Predicate<Order> checkUser(long userId){
        return order -> {
            if(userId==-1){
                return true;
            }
            if(order.getUser().getUserId()== userId){
                return true;
            }
            return false;
        };
    }

    public Predicate<Order> checkStatus(String status){
        return order -> {
            if(status.equals("")){
                return true;
            }
            if(order.getStatus().equals(status)){
                return true;
            }
            return false;
        };
    }
    public static List<Order> filterOrder (List<Order> orders,
                                           Predicate<Order> predicate)
    {
        return orders.stream()
                .filter( predicate )
                .collect(Collectors.<Order>toList());
    }
}
