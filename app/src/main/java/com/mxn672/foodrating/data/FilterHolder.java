package com.mxn672.foodrating.data;

import com.mxn672.foodrating.data.api.BusinessType;
import com.mxn672.foodrating.data.api.Region;

public class FilterHolder {
    public boolean isActive = false;
    public Region region = Region.NULL;
    public BusinessType businessType = BusinessType.NULL;
    public FilterRating ratingOP = FilterRating.NULL;
    public int ratingVAL = -1;
    public Distance maxDistance = Distance.NO_LIMIT;

    public FilterHolder(Region region, BusinessType businessType, FilterRating ratingOP,
                        int ratingVAL, Distance maxDistance) {

        this.region = region;
        this.businessType = businessType;
        this.ratingOP = ratingOP;
        this.ratingVAL = ratingVAL;
        this.maxDistance = maxDistance;
        this.isActive = true;
    }

    public FilterHolder(){
    }
}
