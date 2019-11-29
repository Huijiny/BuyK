package com.buyk.crocompany.buyk_android.util;

import android.util.Log;

import java.text.DecimalFormat;

public class ConvertKmOrPrice {
    private String pResult;
    DecimalFormat df = new DecimalFormat("###,###.####");
    public String convertPrice(int price){
        String str = String.format("%,d", price);
        Log.e("str",str);
        return str+"만원";
    }
    public String convertDistance(int distance) {
        String str = String.format("%,d", distance);
        if(str.equalsIgnoreCase("0")){
            return "신차";
        }
        else {
            return str + "km";
        }
    }

}
