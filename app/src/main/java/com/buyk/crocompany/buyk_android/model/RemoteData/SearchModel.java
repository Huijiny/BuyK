package com.buyk.crocompany.buyk_android.model.RemoteData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ahdguialee on 2018. 5. 24..
 */

public class SearchModel {

    @SerializedName("id")
    private int id;

    private String name;

    private Brand brand;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

}

