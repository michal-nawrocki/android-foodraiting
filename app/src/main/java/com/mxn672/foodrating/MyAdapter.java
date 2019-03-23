package com.mxn672.foodrating;

import android.support.v7.widget.RecyclerView;
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
        holder.txtHeader.setText(mDataset.get(position).businessName);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
