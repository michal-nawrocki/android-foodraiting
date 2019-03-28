package com.mxn672.foodrating.data;

public class SortHolder {
    public SortType sortBy = SortType.DISTANCE;
    public boolean ascending = true;

    public SortHolder(SortType sortBy, boolean ascd){
        this.sortBy = sortBy;
        this.ascending = ascd;
    }

    public SortHolder(){

    }
}
