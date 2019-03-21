package com.mxn672.foodrating;

import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {

    private static String url = "http://api.ratings.food.gov.uk/establishments?";
    private JsonObjectRequest getRequest;

    private static JsonObjectRequest getRequest(){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("result", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("result", "Ups, got an error: " + error.getLocalizedMessage());
                    }
                }
        ){
            @Override
            public Map getHeaders(){
                HashMap headers = new HashMap();
                headers.put("x-api-version", "2");
                headers.put("format", "JSON");

                return headers;
            }
        };

        return getRequest;
    }

    public static JsonObjectRequest setByName(String name){
        cleanUp();
        url+= "name=" + name;
        return getRequest();
    }

    public static JsonObjectRequest setByCity(String city){
        cleanUp();
        url+= "address=" + city;
        return getRequest();
    }

    public static JsonObjectRequest setByStreet(String street){
        cleanUp();
        url+= "address=" + street;
        return getRequest();
    }

    public static JsonObjectRequest setByPostcode(String postcode){
        cleanUp();
        url+= "address=" + postcode;
        return getRequest();
    }

    public static JsonObjectRequest setByGPS(String lon, String lat, String radius){
        cleanUp();
        url += "longitude=" + lon + "&latitude=" + lat + "&maxDistanceLimit=" + radius;
        return getRequest();
    }

    private static void cleanUp(){
        url = "http://api.ratings.food.gov.uk/establishments?";
    }
}
