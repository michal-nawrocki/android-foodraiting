package com.mxn672.foodrating;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mxn672.foodrating.data.Establishment;

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
        TextView nameText = (TextView) view.findViewById(R.id.estb_name);
        nameText.setText(estb.businessName);

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.estb_rating);
        //ratingBar.setRating(estb.rating);

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
