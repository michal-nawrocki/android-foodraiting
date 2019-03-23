package com.mxn672.foodrating.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Establishment {
    public JSONObject data;
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
        this.data = obj;
        try {
            this.businessName = (String) obj.get("BusinessName");
            this.rating = (String) obj.get("RatingValue");
            this.address_l1 = obj.get("AddressLine1").toString() + " " + obj.get("AddressLine2").toString();
            Log.e("Addres1: ", address_l1);
            this.address_l2 = (String) obj.get("AddressLine2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

};