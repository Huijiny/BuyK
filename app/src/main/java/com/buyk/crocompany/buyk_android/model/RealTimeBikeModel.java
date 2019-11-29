package com.buyk.crocompany.buyk_android.model;

/**
 * Created by ahdguialee on 2018. 6. 4..
 */

public class RealTimeBikeModel {

    String price;
    String year;
    String distance;
    String imageUrl;

    public String getPrice() {
        return price;
    }

    public String getYear() {
        return year;
    }

    public String getDistance() {
        return distance;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
