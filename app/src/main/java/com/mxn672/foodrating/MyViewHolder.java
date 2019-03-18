package com.mxn672.foodrating;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    public TextView txtHeader;
    public TextView txtFooter;

    public MyViewHolder(View v) {
        super(v);
        txtHeader = (TextView) v.findViewById(R.id.row_Name);
        txtFooter = (TextView) v.findViewById(R.id.row_Address);
    }
}
