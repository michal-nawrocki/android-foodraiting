package com.mxn672.foodrating.data;

import android.util.Log;

public class QueryHolder {
    public static String url = "http://api.ratings.food.gov.uk/establishments?";
    public QueryType type;
    public String queryKeyword;
    public QueryDistance radius;
    public int pageSize = 20;
    public int pageNumber = 1;
    public Double lon = null;
    public Double lat = null;

    public QueryHolder(QueryType type, String keyword, QueryDistance radius, double lon, double lat) {
        this.type = type;
        this.queryKeyword = keyword;
        this.radius = radius;
        this.lon = lon;
        this.lat = lat;
    }

    public String getQueryURL() {
        String query = url;

        // apply the keyword based on QueryType
        switch (type) {
            case POSTCODE:
                query += "address=" + queryKeyword;
                break;
            case STREET:
                query += "address=" + queryKeyword;
                break;
            case NAME:
                query += "name=" + queryKeyword;
                break;
            case CITY:
                query += "address=" + queryKeyword;
                break;
        }


        // apply extra stuff
        query+= "&latitude=" + lat + "&longitude=" +  lon +
                "&pageNumber=" + pageNumber+ "&pageSize=" + pageSize ;

        if(!radius.equals(QueryDistance.NO_LIMIT)){
            query += "&maxDistanceLimit=" + radius.getMiles();
        }

        Log.e("QueryLink:", query);
        return query;
    }

    public void setUpdatedQuery(QueryType type, String keyword){
        this.type = type;
        this.queryKeyword = keyword;
    }

    public void setRadius(QueryDistance radius){
        this.radius = radius;
    }

    public void setNextPage(){
       pageNumber++;
    }

    public void setPrevPage(){
        pageNumber = (pageNumber == 1)? 1 : pageNumber--;
    }
}
