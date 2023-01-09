package com.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormat {
    //Wed Apr 22 05:12:10 CST 1989格式的java.util.Date转为1989-04-22格式的java.sql.Date
    public java.sql.Date DateToDate(java.util.Date utilDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(utilDate);
        java.util.Date date = null;
        try {
            date = sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new java.sql.Date(date.getTime());
    }
}
