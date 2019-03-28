package com.mxn672.foodrating.data;

import com.mxn672.foodrating.data.api.BusinessType;

public class FilterHolder {
    public boolean isActive = false;
    public BusinessType businessType = BusinessType.NULL;
    public FilterRating ratingOP = FilterRating.NULL;
    public int ratingVAL = -1;
    public Distance maxDistance = Distance.NO_LIMIT;
    public boolean removeNotRated = true;

    public FilterHolder(BusinessType businessType, FilterRating ratingOP,
                        int ratingVAL, Distance maxDistance, boolean removeNotRated) {

        this.businessType = businessType;
        this.ratingOP = ratingOP;
        this.ratingVAL = ratingVAL;
        this.maxDistance = maxDistance;
        this.isActive = true;
        this.removeNotRated = removeNotRated;
    }

    public FilterHolder(){
    }
}
