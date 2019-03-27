package com.mxn672.foodrating.data;

public enum QueryDistance {
    ONE_MILE(1), TWO_MILES(2), THREE_MILES(3), FIVE_MILES(5), TEN_MILES(10), NO_LIMIT(-1);

    private int distance;

     QueryDistance(int val){
        this.distance = val;
    }

    public int getMiles(){
        return this.distance;
    }
}
