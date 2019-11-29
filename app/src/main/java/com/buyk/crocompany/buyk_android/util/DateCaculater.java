package com.buyk.crocompany.buyk_android.util;

import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
/**
 * Created by leeyun on 2018. 7. 15..
 */

public class DateCaculater {

    long calculatedValue;
    public long Caculater(String reqTime){
        Log.e("데드라인",reqTime);
        //현재시간
          Date curDate = new Date();
            long curTime = System.currentTimeMillis();


         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //요청시간을 Date로 parsing 후 time가져오기
        Date reqDate = new Date();
        try {
            reqDate = dateFormat.parse(reqTime);
            Log.e("데드라인-", String.valueOf(reqDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long reqDateTime = reqDate.getTime(); //현재시간을 요청시간의 형태로 format 후 time 가져오기
        try {
            curDate = dateFormat.parse(dateFormat.format(curTime));
            Log.e("현재시간", String.valueOf(curDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long curDateTime = curDate.getTime(); //분으로 표현
        calculatedValue = (reqDateTime - curDateTime) / 60000;
        Log.e("데드라인 = ", String.valueOf(reqDateTime));

        Log.e("현재시간 = ", String.valueOf(curDateTime));

        Log.e("데드라인-현재시간 = ", String.valueOf(calculatedValue));
        return  calculatedValue;
    }

    public long timeConverterNow(String reqTime){
        Log.e("데드라인",reqTime);
        //현재시간
        Date curDate = new Date();
        long curTime = System.currentTimeMillis();


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //요청시간을 Date로 parsing 후 time가져오기
        Date reqDate = new Date();
        try {
            reqDate = dateFormat.parse(reqTime);
            Log.e("데드라인-", String.valueOf(reqDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long reqDateTime = reqDate.getTime(); //현재시간을 요청시간의 형태로 format 후 time 가져오기
        try {
            curDate = dateFormat.parse(dateFormat.format(curTime));
            Log.e("현재시간", String.valueOf(curDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long curDateTime = curDate.getTime(); //분으로 표현
        calculatedValue = (curDateTime -reqDateTime);
        calculatedValue = TimeUnit.MILLISECONDS.toSeconds(calculatedValue);
        Log.e("TimeSecond", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(calculatedValue)));

        Log.e("데드라인-현재시간 = ", String.valueOf(calculatedValue));
        return  calculatedValue;
    }
    public String convertRecentTime(String createdTime, Long reqTime) {
        int idx = createdTime.indexOf("T");
        createdTime = createdTime.substring(0,idx);
        if ( reqTime < 60) {
                return "방금";
            } else if (reqTime < 3600 ){
                String n = String.valueOf(reqTime/60);
                return n+"분 전";
            } else if (reqTime < 86400) {
                String n = String.valueOf(reqTime/3600);
                return n+"시간 전";
            } else if (reqTime < 172800) {
                return "어제";
            } else if (reqTime < 604800) {
                String n = String.valueOf(reqTime/86400);
                return n+"일 전";
            } else if (reqTime < 1209600) {
                return "일주일 전";
            }else {
                    return createdTime;
                }
    }
}
