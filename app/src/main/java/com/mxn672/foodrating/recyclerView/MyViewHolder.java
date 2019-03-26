package com.mxn672.foodrating.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mxn672.foodrating.R;

public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView txtHeader;
    public TextView txtFooter;
    public RatingBar ratingBar;
    public TextView ratingError;
    public ImageButton favourite;
    public Button rowButton;

    public MyViewHolder(View v) {
        super(v);
        txtHeader = (TextView) v.findViewById(R.id.row_Name);
        txtFooter = (TextView) v.findViewById(R.id.row_Address);
        ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
        ratingError = (TextView) v.findViewById(R.id.rating_error);
        favourite = (ImageButton) v.findViewById(R.id.row_favButton);
        rowButton = (Button) v.findViewById(R.id.row_button);
    }
}
