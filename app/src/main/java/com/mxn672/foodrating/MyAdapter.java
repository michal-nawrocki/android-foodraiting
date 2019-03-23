package com.mxn672.foodrating;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mxn672.foodrating.data.Establishment;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<Establishment> mDataset;


    public MyAdapter(ArrayList<Establishment> dataSet) {
        mDataset = dataSet;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        String eName = new String();
        Integer eRating = -1;
        holder.ratingError.setVisibility(View.INVISIBLE);

        try{
            eName = mDataset.get(position).businessName;
            eRating= Integer.parseInt(mDataset.get(position).rating);

        }catch (NumberFormatException e){
            eRating = -1;
        }catch(NullPointerException e){
            eName = "";
        }

        if(eName.length() >= 30){
            holder.txtHeader.setText(eName.substring(0,30) + "...");
        }else{
            holder.txtHeader.setText(eName);
        }

        if(0 <= eRating &&  eRating <= 5 ){
            holder.ratingBar.setRating(eRating);
        }else{
            holder.ratingBar.setVisibility(View.INVISIBLE);
            holder.ratingError.setVisibility(View.VISIBLE);
            holder.ratingError.setText("No rating available");
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
