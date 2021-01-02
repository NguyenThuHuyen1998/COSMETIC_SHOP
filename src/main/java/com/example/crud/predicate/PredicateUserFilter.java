package com.example.crud.predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    created by HuyenNgTn on 12/12/2020
*/
public class PredicateUserFilter {
    public static final Logger logger = LoggerFactory.getLogger(PredicateUserFilter.class);

    private static final PredicateUserFilter predicateUserFilter= new PredicateUserFilter();

    private PredicateUserFilter(){

    }

    private static PredicateUserFilter getInstance(){
        return predicateUserFilter;
    }


}
