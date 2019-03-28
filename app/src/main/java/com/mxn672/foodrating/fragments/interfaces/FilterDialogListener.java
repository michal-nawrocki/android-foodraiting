package com.mxn672.foodrating.fragments.interfaces;

import com.mxn672.foodrating.data.FilterHolder;
import com.mxn672.foodrating.data.SortHolder;
import com.mxn672.foodrating.data.Distance;
import com.mxn672.foodrating.data.QueryType;

public interface FilterDialogListener {
    void onDialogPositiveClick(QueryType qr_type, Distance qr_distance, SortHolder sortBy, FilterHolder filter);
}
