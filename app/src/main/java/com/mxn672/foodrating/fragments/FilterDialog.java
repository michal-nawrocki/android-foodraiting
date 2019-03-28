package com.mxn672.foodrating.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.FilterHolder;
import com.mxn672.foodrating.data.FilterRating;
import com.mxn672.foodrating.data.SortHolder;
import com.mxn672.foodrating.data.SortType;
import com.mxn672.foodrating.data.Distance;
import com.mxn672.foodrating.data.QueryType;
import com.mxn672.foodrating.data.api.BusinessType;
import com.mxn672.foodrating.data.api.Region;
import com.mxn672.foodrating.fragments.interfaces.FilterDialogListener;

public class FilterDialog extends DialogFragment{
    private View view;
    private QueryType searchBy = QueryType.NAME;
    private Distance maxDistance = Distance.THREE_MILES;
    private SortType sortType = SortType.DISTANCE;
    private SortHolder sorter;
    private boolean sortAscedning = false;
    private FilterHolder filter;
    private BusinessType businessType = BusinessType.NULL;
    private FilterRating filterRating = FilterRating.NULL;
    private int filterRatingNumber = -1;
    private Distance filterDistance = Distance.NO_LIMIT;
    private Boolean removeNotRated = false;

    // Save preferences using this
    SharedPreferences.Editor editor;
    // Load preferences using this
    SharedPreferences prefs;


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
        editor = getActivity().getPreferences(0).edit();
        prefs = getActivity().getPreferences(0);

        // Spinner SearchBy setup
        Spinner fSearchBy = view.findViewById(R.id.search_typeSpinner);
        ArrayAdapter<CharSequence> adapter_fSearchBy = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_searchBy, android.R.layout.simple_spinner_item);
        adapter_fSearchBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fSearchBy.setAdapter(adapter_fSearchBy);
        fSearchBy.setSelection(prefs.getInt("searchBy_selected", 0));
        fSearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store the preference
                editor.putInt("searchBy_selected", fSearchBy.getSelectedItemPosition());
                editor.apply();

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
                    case "Location":
                        searchBy = QueryType.LOCATION;
                }
                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Spinner MaxDistance setup
        Spinner fMaxDistance = view.findViewById(R.id.search_distanceSpinner);
        ArrayAdapter<CharSequence> adapter_fMaxDistance = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_maxDistance, android.R.layout.simple_spinner_item);
        adapter_fMaxDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fMaxDistance.setAdapter(adapter_fMaxDistance);
        fMaxDistance.setSelection(prefs.getInt("maxDistance_selected", 0));
        fMaxDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("maxDistance_selected", fMaxDistance.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding QueryDistance
                switch (result){
                    case "1 mi.":
                        maxDistance = Distance.ONE_MILE;
                        break;
                    case "2 mi.":
                        maxDistance = Distance.TWO_MILES;
                        break;
                    case "3 mi.":
                        maxDistance = Distance.THREE_MILES;
                        break;
                    case "5 mi.":
                        maxDistance = Distance.FIVE_MILES;
                        break;
                    case "10 mi.":
                        maxDistance = Distance.TEN_MILES;
                        break;
                    case "No limit":
                        maxDistance = Distance.NO_LIMIT;
                        break;
                }

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Spinner SortBy setup
        Spinner fsortBy = view.findViewById(R.id.sort_sortSpinner);
        ArrayAdapter<CharSequence> adapter_fFilterBy = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_sortBy, android.R.layout.simple_spinner_item);
        adapter_fFilterBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fsortBy.setAdapter(adapter_fFilterBy);
        fsortBy.setSelection(prefs.getInt("filterBy_selected", 3));
        fsortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("filterBy_selected", fsortBy.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding SortType
                switch (result){
                    case "Business Type":
                        sortType = SortType.TYPE;
                        break;
                    case "Inspection Date":
                        sortType = SortType.DATE;
                        break;
                    case "NAME":
                        sortType = SortType.NAME;
                        break;
                    case "Distance":
                        sortType = SortType.DISTANCE;
                        break;
                    case "Rating":
                        sortType = SortType.RATING;
                        break;
                }

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Spinner sortOrder setup

        Spinner fOrder = view.findViewById(R.id.sort_orderSpinner);
        ArrayAdapter<CharSequence> adapter_fOrder = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_order, android.R.layout.simple_spinner_item);
        adapter_fOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fOrder.setAdapter(adapter_fOrder);
        fOrder.setSelection(prefs.getInt("sort_order", 0));
        fOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("sort_order", fOrder.getSelectedItemPosition());
                editor.apply();

                // Set the value
                sortAscedning = (result.equals("Ascending"));

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner type setup
        Spinner fType = view.findViewById(R.id.filter_typeSpinner);
        ArrayAdapter<CharSequence> adapter_fType = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_businessType, android.R.layout.simple_spinner_item);
        adapter_fType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fType.setAdapter(adapter_fType);
        fType.setSelection(prefs.getInt("filter_type", 0));
        fType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("filter_type", fType.getSelectedItemPosition());
                editor.apply();

                switch(result){
                    case "Bars":
                        businessType = BusinessType.BAR;
                        break;
                    case "Distributors":
                        businessType = BusinessType.DISTRIBUTORS;
                        break;
                    case "Farmers":
                        businessType = BusinessType.FARMERS;
                        break;
                    case "Hospitals":
                        businessType = BusinessType.HOSPITAS;
                        break;
                    case "Hotels":
                        businessType = BusinessType.HOTELS;
                        break;
                    case "Importers":
                        businessType = BusinessType.IMPORTERS;
                        break;
                    case "Manufactures":
                        businessType = BusinessType.MANUFACTURES;
                        break;
                    case "Mobile":
                        businessType = BusinessType.MOBILE;
                        break;
                    case "Other":
                        businessType = BusinessType.OTHER;
                        break;
                    case "Restaurants":
                        businessType = BusinessType.RESTAURANT;
                        break;
                    case "Retailers":
                        businessType = BusinessType.RETAILERS;
                        break;
                    case "Schools":
                        businessType = BusinessType.SCHOOL;
                        break;
                    case "Supermarkets":
                        businessType = BusinessType.SUPERMARKETS;
                        break;
                    case "Takeaways":
                        businessType = BusinessType.TAKEAWAYS;
                        break;
                    case "Empty":
                        businessType = BusinessType.NULL;
                        break;
                }
                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner ratingOP setup
        Spinner fRatingOP = view.findViewById(R.id.filter_ratingOPSpinner);
        ArrayAdapter<CharSequence> adapter_fRatingOP= ArrayAdapter.createFromResource(getActivity(),
                R.array.array_ratingOP, android.R.layout.simple_spinner_item);
        adapter_fRatingOP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fRatingOP.setAdapter(adapter_fRatingOP);
        fRatingOP.setSelection(prefs.getInt("filter_ratingOP", 0));
        fRatingOP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("filter_ratingOP", fRatingOP.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding operator
                switch (result){
                    case "Equal":
                        filterRating = FilterRating.EQUAL;
                        break;
                    case "Greater":
                        filterRating = FilterRating.GREATER;
                        break;
                    case "Smaller":
                        filterRating = FilterRating.SMALLER;
                        break;
                    case "Disable":
                        filterRating = FilterRating.NULL;
                        break;
                }
                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner ratingVAL setup
        Spinner fRatingVAL = view.findViewById(R.id.filter_ratingVALSpinner);
        String[] values = new String[]{"0","1","2","3","4","5"};
        ArrayAdapter<String> adapter_fRatingVAL = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, values);
        adapter_fRatingVAL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fRatingVAL.setAdapter(adapter_fRatingVAL);
        fRatingVAL.setSelection(prefs.getInt("filter_ratingVAL", 5));
        fRatingVAL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("filter_ratingVAL", fRatingVAL.getSelectedItemPosition());
                editor.apply();

                switch (result){
                    case "0":
                        filterRatingNumber = 0;
                        break;
                    case "1":
                        filterRatingNumber = 1;
                        break;
                    case "2":
                        filterRatingNumber = 2;
                        break;
                    case "3":
                        filterRatingNumber = 3;
                        break;
                    case "4":
                        filterRatingNumber = 4;
                        break;
                    case "5":
                        filterRatingNumber = 5;
                        break;
                }

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner filterDistance setup
        Spinner fFilterDistance = view.findViewById(R.id.filter_distanceSpinner);
        ArrayAdapter<CharSequence> adapter_fFilterDistance = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_maxDistance, android.R.layout.simple_spinner_item);
        adapter_fFilterDistance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fFilterDistance.setAdapter(adapter_fFilterDistance);
        fFilterDistance.setSelection(prefs.getInt("filter_distance", 3));
        fFilterDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("filter_distance", fFilterDistance.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding QueryDistance
                switch (result){
                    case "1 mi.":
                        filterDistance = Distance.ONE_MILE;
                        break;
                    case "2 mi.":
                        filterDistance = Distance.TWO_MILES;
                        break;
                    case "3 mi.":
                        filterDistance = Distance.THREE_MILES;
                        break;
                    case "5 mi.":
                        filterDistance = Distance.FIVE_MILES;
                        break;
                    case "10 mi.":
                        filterDistance = Distance.TEN_MILES;
                        break;
                    case "No limit":
                        filterDistance = Distance.NO_LIMIT;
                        break;
                }

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Box removeNoRating setup
        CheckBox fRemoveNoRating = view.findViewById(R.id.filter_removeNoRating);
        Log.e("Checkbox val: ", String.valueOf(prefs.getBoolean("removeNot", true)));
        removeNotRated = prefs.getBoolean("removeNot", true);
        fRemoveNoRating.setChecked(removeNotRated);


        fRemoveNoRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Checkbox val: ", String.valueOf(prefs.getBoolean("removeNot", false)));

                if(fRemoveNoRating.isChecked()){
                    removeNotRated = true;
                    editor.putBoolean("removeNot", true).commit();
                }else{
                    removeNotRated = false;
                    editor.putBoolean("removeNot", false).commit();
                }

                Log.e("Filter remove", String.valueOf(fRemoveNoRating.isChecked()));
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Apply",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                filter = new FilterHolder(businessType, filterRating, filterRatingNumber, filterDistance, prefs.getBoolean("removeNot", false));
                sorter = new SortHolder(sortType, sortAscedning);

                listener.onDialogPositiveClick(searchBy, maxDistance, sorter, filter);
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
