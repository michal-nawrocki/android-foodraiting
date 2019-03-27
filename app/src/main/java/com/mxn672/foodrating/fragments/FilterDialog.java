package com.mxn672.foodrating.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.QueryDistance;
import com.mxn672.foodrating.data.QueryType;
import com.mxn672.foodrating.fragments.interfaces.FilterDialogListener;

public class FilterDialog extends DialogFragment{
    private View view;
    private QueryType searchBy = QueryType.NAME;
    private QueryDistance maxDistance = QueryDistance.THREE_MILES;

    public FilterDialog() {

    }

    // Use this instance of the interface to deliver action events
    FilterDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (FilterDialogListener) context;
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
        View view = inflater.inflate(R.layout.fragment_filter, null);


        // Spinner SearchBy setup
        Spinner fSearchBy = view.findViewById(R.id.filter_typeSpinner);
        ArrayAdapter<CharSequence> adapter_fSearchBy = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_filterBy, android.R.layout.simple_spinner_item);
        adapter_fSearchBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fSearchBy.setAdapter(adapter_fSearchBy);
        fSearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Get the corresponding QueryType
                switch (result){
                    case "Name":
                        searchBy = QueryType.NAME;
                        break;
                    case "Street":
                        searchBy = QueryType.STREET;
                        break;
                    case "City":
                        searchBy = QueryType.CITY;
                        break;
                    case "Post Code":
                        searchBy = QueryType.POSTCODE;
                        break;
                }
                Log.e("Spinner of " + view.getId(), "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // Spinner MaxDistance setup
        Spinner fMaxDistance = view.findViewById(R.id.filter_distanceSpinner);
        ArrayAdapter<CharSequence> adapter_fMaxDistance = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_maxDistance, android.R.layout.simple_spinner_item);
        adapter_fMaxDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fMaxDistance.setAdapter(adapter_fMaxDistance);
        fMaxDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Get the corresponding QueryDistance
                switch (result){
                    case "1 mi.":
                        maxDistance = QueryDistance.ONE_MILE;
                        break;
                    case "2 mi.":
                        maxDistance = QueryDistance.TWO_MILES;
                        break;
                    case "3 mi.":
                        maxDistance = QueryDistance.THREE_MILES;
                        break;
                    case "5 mi.":
                        maxDistance = QueryDistance.FIVE_MILES;
                        break;
                    case "10 mi.":
                        maxDistance = QueryDistance.TEN_MILES;
                        break;
                    case "No limit":
                        maxDistance = QueryDistance.NO_LIMIT;
                        break;
                }

                Log.e("Spinner of " + view.getId(), "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setView(view);
        builder.setPositiveButton("Apply",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success

                listener.onDialogPositiveClick(searchBy, maxDistance);
            }
        });

        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
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
