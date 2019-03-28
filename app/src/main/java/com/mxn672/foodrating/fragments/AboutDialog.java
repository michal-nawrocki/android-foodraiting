package com.mxn672.foodrating.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.mxn672.foodrating.R;

public class AboutDialog extends DialogFragment {

    public AboutDialog(){

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_about, null);
        TextView credits = v.findViewById(R.id.credits);

        credits.setText("For Mobile & Ubiquitous Computing\n" +
                "\n" +
                "By Michal Nawrocki\n" +
                "©2019");
        builder.setView(v);
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        return builder.create();
    }
}
