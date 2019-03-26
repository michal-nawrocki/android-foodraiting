package com.mxn672.foodrating.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.Establishment;

import org.w3c.dom.Text;

@SuppressLint("ValidFragment")
public class EstablishmentFragment extends DialogFragment {

    private Establishment estb;

    public EstablishmentFragment(Establishment estb){
        this.estb = estb;
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
        eType = estb.businessType;
        ePostCode = estb.address_postcode;

        try{
            eRating = Integer.parseInt(estb.rating);
        }catch (NumberFormatException e){
            eRating = -1;
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
        vPostCode.setText(ePostCode);

        // Attach data to the builder
        builder.setTitle(estb.businessName);
        builder.setView(view);
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });

        return builder.create();
    }
}
