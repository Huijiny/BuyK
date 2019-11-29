package com.buyk.crocompany.buyk_android.model.RemoteData;

public class ItemUpdatePatch {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    String deal_status;


    public String getDeal_status() {
        return deal_status;
    }

    public void setDeal_status(String deal_status) {
        this.deal_status = deal_status;
    }
}
