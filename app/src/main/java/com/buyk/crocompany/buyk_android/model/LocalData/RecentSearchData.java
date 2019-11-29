package com.buyk.crocompany.buyk_android.model.LocalData;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by ahdguialee on 2018. 6. 18..
 */

public class RecentSearchData extends RealmObject {

    @SerializedName("id")
    private int id;

    private String name;

    private String brandEnglishName;
    private String brandName;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandEnglishName() {
        return brandEnglishName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandEnglishName(String brandEnglishName) {
        this.brandEnglishName = brandEnglishName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
