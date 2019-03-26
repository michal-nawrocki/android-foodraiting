package com.mxn672.foodrating.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

@Entity
public class Establishment {

    @PrimaryKey
    @NonNull
    public String estb_id;

    @ColumnInfo(name = "book_title")
    public String businessName;

    @Ignore
    public JSONObject data;
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
    public String date;
    public boolean favoured;

    public Establishment(String businessName){
        this.businessName = businessName;
    }

    public Establishment(JSONObject obj){
        DecimalFormat df2 = new DecimalFormat("#.##");
        this.data = obj;
        try {
            this.estb_id = (String) String.valueOf(obj.get("FHRSID"));
            this.businessName = (String) obj.get("BusinessName");
            this.businessType = (String) obj.get("BusinessType");
            this.rating = (String) obj.get("RatingValue");
            this.address_l1 = (String) obj.get("AddressLine1");
            this.address_l2 = (String) obj.get("AddressLine2");
            this.address_l3 = (String) obj.get("AddressLine3");
            this.address_l4 = (String) obj.get("AddressLine4");
            this.address_postcode = (String) obj.get("PostCode");
            this.distance = df2.format(obj.get("Distance"));
            this.date = (String) obj.get("RatingDate");
            this.favoured = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAddress_l1(){
        return address_l1;
    }
    public String getAddress_postcode(){
        return address_postcode;
    }
};