package com.mxn672.foodrating.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Establishment {
    public String businessName;
    public String businessType;
    public String address_l1;
    public String address_l2;
    public String address_l3;
    public String address_l4;
    public String address_postcode;
    public String phone;
    public String rating;
    public String lon;
    public String lat;
    public String distance;

    public Establishment(JSONObject obj){
        try {
            this.businessName = (String) obj.get("BusinessName");
            this.rating = (String) obj.get("RatingValue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Establishment(String businessName){

        this.businessName = businessName;
    }

    public Establishment(String businessName, String rating){
        this.businessName = businessName;
        this.rating = rating;
    }
};