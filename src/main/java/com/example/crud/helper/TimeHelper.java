package com.example.crud.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/*
    created by HuyenNgTn on 12/12/2020
*/
public class TimeHelper {
    public static final Logger logger = LoggerFactory.getLogger(TimeHelper.class);

    private static TimeHelper timeHelper= new TimeHelper();
    private static Calendar calendar= Calendar.getInstance();
    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static TimeHelper getInstance(){
        return timeHelper;
    }

    public long convertTimestamp(String dateStr) throws ParseException {
        Date date= dateFormat.parse(dateStr);
        return date.getTime();
    }


    public String getNow(){
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public String getDate(long timestamp){
        Date date= new Date(timestamp);
        return simpleDateFormat.format(date);
    }


    public String getFirstDayInWeek(){
        // ngày đầu tiên trong tuần là chủ nhật tuần trước
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        String dateStart= simpleDateFormat.format(calendar.getTime());
        return dateStart;
    }

    public String getLastDayInWeek(){
        // ngày cuối cùng của tuần là t7
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY){
            calendar.set(Calendar.DAY_OF_YEAR, ++day);
        }
        String dateEnd= simpleDateFormat.format(calendar.getTime());
        return dateEnd;
    }


    public String getFirstInMonth(){
        int month= calendar.get(Calendar.MONTH)+1;
        int year= calendar.get(Calendar.YEAR);
        if(month<10){
            return "01"+"/0"+String.valueOf(month)+ "/"+ String.valueOf(calendar.get(Calendar.YEAR));
        }
        return "01/"+ String.valueOf(month)+ "/"+ String.valueOf(calendar.get(Calendar.YEAR));
    }

    public String getLastDayInMonth(){
        Calendar calendar= Calendar.getInstance();
        int lastDayOfMonth= calendar.getActualMaximum(Calendar.DATE);
        int month= calendar.get(Calendar.MONTH)+ 1;
        if(month<10){
            return String.valueOf(lastDayOfMonth)+"/0"+String.valueOf(month)+ "/"+ String.valueOf(calendar.get(Calendar.YEAR));
        }
        return String.valueOf(lastDayOfMonth)+"/"+ String.valueOf(month)+ "/"+ String.valueOf(calendar.get(Calendar.YEAR));
    }

    public String getFirstDayOfYear(){
        Calendar calendar= Calendar.getInstance();
        return "01/01/"+ calendar.get(Calendar.YEAR);
    }

    public String getLastDayOfYear(){
        Calendar calendar= Calendar.getInstance();
        return "31/12/"+ calendar.get(Calendar.YEAR);
    }

}

