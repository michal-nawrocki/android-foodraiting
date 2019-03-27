package com.mxn672.foodrating.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.Establishment;
import com.mxn672.foodrating.data.EstablishmentDatabase;
import com.mxn672.foodrating.fragments.interfaces.EstablishmentDialogListener;

@SuppressLint("ValidFragment")
public class EstablishmentFragment extends DialogFragment {

    private Establishment estb;
    private EstablishmentDatabase db;

    public EstablishmentFragment(Establishment estb, EstablishmentDatabase db){
        this.estb = estb;
        this.db = db;
    }


    // Use this instance of the interface to deliver action events
    EstablishmentDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (EstablishmentDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_establishment, null);

        // Setup all the view and populate them with the data of estb
        TextView vAddres = view.findViewById(R.id.estb_address);
        TextView vType = view.findViewById(R.id.estb_type);
        TextView vDate = view.findViewById(R.id.estb_inspectionDate);
        TextView vError = view.findViewById(R.id.estb_ratingError);
        RatingBar vRating = view.findViewById(R.id.estb_rating);
        TextView vPostCode = view.findViewById(R.id.estb_post);
        Switch vFav = view.findViewById(R.id.estb_favButton);
        Boolean isFavoured = estb.favoured;

        // Populate all views
        String eName = new String();
        Integer eRating = -1;
        String eAddress = new String();
        String ePostCode = new String();
        String eID = new String();
        String eDate = new String();
        String eType = new String();

        eID = estb.estb_id;
        eName = estb.businessName;
        eAddress = estb.getAddress_l1() + '\n' + estb.address_l2 + "\n" + estb.address_l3
                + "\n" + estb.address_l4;
        eDate = estb.date;
        ePostCode = estb.getAddress_postcode();
        eType = estb.businessType;

        try{
            eRating = Integer.parseInt(estb.rating);
            vPostCode.setText(ePostCode);
        }catch (NumberFormatException e){
            eRating = -1;
        }catch (Error e){
            Log.e("Postcode", e.getLocalizedMessage());
        }

        if(eRating == -1){
            vRating.setVisibility(View.INVISIBLE);
            vError.setText(estb.rating);
            vError.setVisibility(View.VISIBLE);
        }else{
            vRating.setRating(eRating);
        }

        vAddres.setText(eAddress);
        if(eDate.isEmpty() || eDate.substring(0,10).equals("1901-01-01")){
            vDate.setText("No date");
        }else{
            vDate.setText(eDate.substring(0,10));
        }
        vType.setText(eType);

        // Check for favoured state
        if(estb.favoured) vFav.setChecked(true);

        // Attach data to the builder
        if(estb.businessType.contains("Restaurant")){
            builder.setIcon(R.drawable.restaurant);

        }else if(estb.businessType.contains("Takeaway")){
            builder.setIcon(R.drawable.takeaway);
        }
        builder.setTitle(estb.businessName);
        builder.setView(view);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(vFav.isChecked() != isFavoured){
                    if(vFav.isChecked()){
                        //We add to the DB
                        estb.favoured = true;
                        db.establishmentDao().insertEstablishment(estb);

                    }else{
                        //We remove from the DB
                        estb.favoured = false;
                        db.establishmentDao().deleteEstablishment(estb);
                    }
                }

                listener.onDialogPositiveClick(estb);
            }
        });

        return builder.create();
    }
}
