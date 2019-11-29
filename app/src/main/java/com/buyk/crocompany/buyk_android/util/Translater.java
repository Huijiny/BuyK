package com.buyk.crocompany.buyk_android.util;

import com.buyk.crocompany.buyk_android.soldBikeView;

public class Translater {
    public String locationTranslate(String region){
        switch (region){
            case "서울":
                return "SU";
            case "부산":
                return "BS";
            case "인천":
                return "IC";
            case "광주":
                return "GJ";
            case "울산":
                return "US";
            case "대전":
                return "DJ";
            case "세종":
                return "SJ";
            case "대구":
                return "DG";
            case "경기":
                return "GG";
            case "강원":
                return "GW";
            case "경남":
                return "GN";
            case "경북":
                return "GB";
            case "충북":
                return "CB";
            case "충남":
                return "CN";
            case "전북":
                return "JB";
            case "전남":
                return "JN";
            case "제주":
                return "JJ";

            default: return "";
        }
    }
}
