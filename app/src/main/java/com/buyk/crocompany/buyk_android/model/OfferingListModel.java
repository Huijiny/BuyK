package com.buyk.crocompany.buyk_android.model;

import com.buyk.crocompany.buyk_android.model.RemoteData.Image;

import java.util.List;

/**
 * Created by ahdguialee on 2018. 5. 15..
 */

public class OfferingListModel {
    private String bikeYear;
    private String bikePrice;
    private String bikeUrl;

    public String getBikeYear() {
        return bikeYear;
    }

    public String getBikePrice() {
        return bikePrice;
    }

    public void setBikeYear(String bikeYear) {
        this.bikeYear = bikeYear;
    }

    public void setBikePrice(String bikePrice) {
        this.bikePrice = bikePrice;
    }

}
