package com.mxn672.foodrating;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.mxn672.foodrating.data.Establishment;

import org.json.JSONException;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    public MyAdapter(ArrayList<Establishment> dataSet) {
        mDataset = dataSet;
    }

    private ArrayList<Establishment> mDataset;
    private Filter establishmentsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Establishment> filteredEstablishments = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredEstablishments.addAll(mDataset);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Establishment e : mDataset){
                    if(e.businessName.toLowerCase().contains(filterPattern)){
                        filteredEstablishments.add(e);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredEstablishments;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataset.clear();
            mDataset.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


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
        String eAddress = new String();

        try{
            eName = mDataset.get(position).businessName;
            eRating = Integer.parseInt(mDataset.get(position).rating);
            eAddress = mDataset.get(position).getAddress_l1();

        }catch (NumberFormatException e){
            eRating = -1;
        }catch(NullPointerException e){
            eName = "";
        }

        if(eName.length() >= 35){
            holder.txtHeader.setText(eName.substring(0,35) + "...");
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

        // Very bad practise but somehow this fucking thing works
        if(eAddress.isEmpty()){
            eAddress = mDataset.get(position).address_l1;
            if(eAddress.length() == 0) eAddress = mDataset.get(position).address_l2;
        }

        holder.txtFooter.setText(eAddress);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public Filter getFilter() {
        return establishmentsFilter;
    }


}
