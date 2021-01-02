package com.example.crud.predicate;

import com.example.crud.entity.Order;
import com.example.crud.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;

/*
    created by HuyenNgTn on 12/12/2020
*/
public class PredicateProductFilter {
    public static final Logger logger = LoggerFactory.getLogger(PredicateProductFilter.class);

    private static final PredicateProductFilter predicateFilter= new PredicateProductFilter();

    private PredicateProductFilter(){

    }

    public static PredicateProductFilter getInstance(){
        return predicateFilter;
    }

    public Predicate<Product> checkDateAdd(String dateStart, String dateEnd) throws ParseException {
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
        return (product) ->{
            try{
                if(finalTimeStart == -1){
                    if(finalTimeEnd ==-1){
                        return true;
                    }
                    if(product.getDate() <= finalTimeEnd){
                        return true;
                    }
                }
                else if(finalTimeEnd== -1){
                    if (product.getDate() >= finalTimeStart){
                        return true;
                    }
                }
                else if(product.getDate() >= finalTimeStart && product.getDate() <= finalTimeEnd){
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

    public Predicate<Product> checkPrice(double minPrice, double maxPrice){
        return (product) ->{
            try{
                if(minPrice== -1){
                    if(maxPrice ==-1){
                        return true;
                    }
                    if(product.getPrice() <= maxPrice){
                        return true;
                    }
                }
                else if(maxPrice== -1){
                    if (product.getPrice() >= minPrice){
                        return true;
                    }
                }
                else if(product.getPrice() >= minPrice && product.getPrice() <= maxPrice){
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

    public Predicate<Product> checkKeyword(String keyword){
        return (product) ->{
            if(product.getName().toLowerCase().contains(keyword.toLowerCase()) || product.getDescription().toLowerCase().contains(keyword.toLowerCase())  || product.getPreview().toLowerCase().contains(keyword.toLowerCase())){
                return true;
            }
            return false;
        };
    }

    public Predicate<Product> checkActive(){
        return (product) ->{
            if(product.isActive()){
                return true;
            }
            return false;
        };
    }

    public Predicate<Product> checkCategory(long categoryId){
        return product -> {
            if(categoryId==0){
                return true;
            }
            if(product.getCategory().getId()== categoryId){
                return true;
            }
            return false;
        };
    }

}
