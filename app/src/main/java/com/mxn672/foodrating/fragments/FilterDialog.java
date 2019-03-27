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
import android.widget.Spinner;

import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.SortType;
import com.mxn672.foodrating.data.QueryDistance;
import com.mxn672.foodrating.data.QueryType;
import com.mxn672.foodrating.data.api.BusinessType;
import com.mxn672.foodrating.data.api.Region;
import com.mxn672.foodrating.fragments.interfaces.FilterDialogListener;

public class FilterDialog extends DialogFragment{
    private View view;
    private QueryType searchBy = QueryType.NAME;
    private QueryDistance maxDistance = QueryDistance.THREE_MILES;
    private SortType sortType = SortType.DISTANCE;
    private Region filterRegion = Region.NULL;
    private BusinessType businessType = BusinessType.NULL;

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

                // Get the corresponding QueryDistance
                switch (result){
                    case "Business Type":
                        sortType = SortType.TYPE;
                        break;
                    case "Inspection Date":
                        sortType = SortType.DATE;
                        break;
                    case "Local Authority":
                        sortType = SortType.AUTHORITY;
                        break;
                    case "Distance":
                        sortType = SortType.DISTANCE;
                        break;
                    case "Rating":
                        sortType = SortType.RATING;
                        break;
                    case "Region":
                        sortType = SortType.REGION;
                        break;
                }

                Log.e("Spinner of " + id, "Value: " + result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Spinner region setup
        Spinner fRegion = view.findViewById(R.id.filter_regionSpinner);
        ArrayAdapter<CharSequence> adapter_fRegion = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_region, android.R.layout.simple_spinner_item);
        fRegion.setAdapter(adapter_fRegion);
        fRegion.setSelection(prefs.getInt("filter_region",5));
        fRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();

                // Store preference
                editor.putInt("filter_region", fRegion.getSelectedItemPosition());
                editor.apply();

                // Get the corresponding Region
                switch (result){
                    case "East Counties":
                        filterRegion = Region.EC;
                        break;
                    case "East Midlands":
                        filterRegion = Region.EM;
                        break;
                    case "London":
                        filterRegion = Region.LDN;
                        break;
                    case "North East":
                        filterRegion = Region.NE;
                        break;
                    case "North West":
                        filterRegion = Region.NW;
                        break;
                    case "South East":
                        filterRegion = Region.SE;
                        break;
                    case "South West":
                        filterRegion = Region.SW;
                        break;
                    case "West Midlands":
                        filterRegion = Region.WM;
                        break;
                    case "Yorkshire and Humberside":
                        filterRegion = Region.YH;
                        break;
                    case "Northern Ireland":
                        filterRegion = Region.NI;
                        break;
                    case "Wales":
                        filterRegion = Region.WALS;
                        break;
                    default:
                        filterRegion = Region.NULL;
                        break;
                }

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
        fRatingOP.setAdapter(adapter_fRatingOP);
        fRatingOP.setSelection(prefs.getInt("filter_ratingOP", 0));
        fRatingOP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner ratingVAL setup
        Spinner fRatingVAL = view.findViewById(R.id.filter_ratingVALSpinner);
        Integer[] values = new Integer[]{0,1,2,3,4,5};
        ArrayAdapter<Integer> adapter_fRatingVAL = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, values);
        fRatingVAL.setAdapter(adapter_fRatingVAL);
        fRatingVAL.setSelection(prefs.getInt("filter_ratingVAL", 5));
        fRatingVAL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

                listener.onDialogPositiveClick(searchBy, maxDistance, sortType);
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
