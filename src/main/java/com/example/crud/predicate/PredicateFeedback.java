package com.example.crud.predicate;

import com.example.crud.entity.FeedBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

/*
    created by HuyenNgTn on 13/12/2020
*/
public class PredicateFeedback {
    public static final Logger logger = LoggerFactory.getLogger(PredicateFeedback.class);

    private static final PredicateFeedback predicateFeedback= new PredicateFeedback();
    public PredicateFeedback(){

    }
    public static PredicateFeedback getInstance(){
        return predicateFeedback;
    }

    public Predicate<FeedBack> checkStar(int star){
        return feedBack -> {
            if(star==-1){
                return true;
            }
            if(feedBack.getStar()==star){
                return true;
            }
            return false;
        };
    }
}
