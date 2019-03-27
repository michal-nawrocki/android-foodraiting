package com.mxn672.foodrating.data.api;

public enum Region {

    EC(1, "East Counties"), EM(2, "East Midlands"), LDN(3, "London"), NE(4, "North East"),
    NW(5, "North West"), SE(6, "South East"), SW(7, "South West"), WM(8, "West Midlands"),
    YH(9, "Yorkshire and Humberside"), NI(10, "Northern Ireland"), SC(11, "Scotland"),
    WALS(12, "Wales");

    public int id;
    public String name;

    Region(int id, String name){
        this.id = id;
        this.name = name;
    }
}
