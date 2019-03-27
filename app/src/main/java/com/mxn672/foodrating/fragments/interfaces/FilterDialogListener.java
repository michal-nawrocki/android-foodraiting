package com.mxn672.foodrating.fragments.interfaces;

import com.mxn672.foodrating.data.SortType;
import com.mxn672.foodrating.data.QueryDistance;
import com.mxn672.foodrating.data.QueryType;

public interface FilterDialogListener {
    void onDialogPositiveClick(QueryType qr_type, QueryDistance qr_distance, SortType filter);
}
