package com.mxn672.foodrating.activities;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.mxn672.foodrating.data.FilterHolder;
import com.mxn672.foodrating.data.FilterRating;
import com.mxn672.foodrating.data.SortHolder;
import com.mxn672.foodrating.data.SortType;
import com.mxn672.foodrating.data.Distance;
import com.mxn672.foodrating.data.QueryHolder;
import com.mxn672.foodrating.data.QueryType;
import com.mxn672.foodrating.data.api.BusinessType;
import com.mxn672.foodrating.data.api.Region;
import com.mxn672.foodrating.fragments.FilterDialog;
import com.mxn672.foodrating.fragments.interfaces.EstablishmentDialogListener;
import com.mxn672.foodrating.fragments.interfaces.FilterDialogListener;
import com.mxn672.foodrating.recyclerView.MyAdapter;
import com.mxn672.foodrating.R;
import com.mxn672.foodrating.recyclerView.SimpleDividerItemDecoration;
import com.mxn672.foodrating.data.Establishment;
import com.mxn672.foodrating.data.EstablishmentDatabase;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements EstablishmentDialogListener, FilterDialogListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private BottomNavigationView bottomNavi;
    private Intent intent;

    private ImageButton filterButton;
    private SearchView searchView;
    private ArrayList<Establishment> establishmentsList = new ArrayList<>();
    private ArrayList<Establishment> favouritedEstb = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private QueryHolder requestQuery;
    private QueryType queryBy = QueryType.NAME;
    private Distance queryDistance = Distance.THREE_MILES;
    private SortHolder sorter = new SortHolder();
    private FilterHolder filter = new FilterHolder();
    private String previousKeyword = new String();
    private double lon;
    private double lat;

    private EstablishmentDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get GPS position for location search
        callPermissions();

        // Get Databse setup
        db = Room.databaseBuilder(getApplicationContext(), EstablishmentDatabase .class, "estb-database").allowMainThreadQueries().build();

        // Bottom Navigation block
        bottomNavi = (BottomNavigationView) findViewById(R.id.bottom_navi);
        bottomNavi.setSelectedItemId(R.id.search);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, bottomNavi, "bottom_navi");

        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.map:
                        intent = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.search:
                        break;

                    case R.id.profile:
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent, options.toBundle());
                        break;

                }
                return true;
            }
        });

        // Filter Button block
        filterButton = findViewById(R.id.searchView_filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        // RecyclerView Block
        recyclerView = (RecyclerView) findViewById(R.id.establishmentList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(establishmentsList, getSupportFragmentManager(), db);
        recyclerView.setAdapter(mAdapter);

        // Search View Block
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                previousKeyword = keyword;
                if(requestQuery != null){
                    requestQuery.setUpdatedQuery(queryBy, keyword, queryDistance);
                }else{
                    requestQuery = new QueryHolder(queryBy, keyword, queryDistance, lon, lat);
                }

                loadRecyclerData(requestQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    public void requestLocationUpdates(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PermissionChecker.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PermissionChecker.PERMISSION_GRANTED){
            fusedLocationProviderClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2);
            locationRequest.setInterval(400000);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult){
                    super.onLocationResult(locationResult);
                    Log.e("Location: ", locationResult.getLastLocation().getLatitude() + " " + locationResult.getLastLocation().getLongitude());
                    lat = locationResult.getLastLocation().getLatitude();
                    lon = locationResult.getLastLocation().getLongitude();
                }
            }, getMainLooper());
        }else{
            callPermissions();
        }
    }

    public void callPermissions(){
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this, permissions, "Location permissions are requiered",
                null,
                new PermissionHandler() {

                    @Override
                    public void onGranted() {
                        requestLocationUpdates();
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions){
                        super.onDenied(context, deniedPermissions);
                        callPermissions();
                    }
                });
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog alertDialog = new FilterDialog();
        alertDialog.show(fm, "filterDialog");
    }

    private void loadRecyclerData(QueryHolder qr){
        findViewById(R.id.establishmentList).setVisibility(View.VISIBLE);
        findViewById(R.id.listError).setVisibility(View.INVISIBLE);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        //progressDialog.show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, qr.getQueryURL(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressDialog.dismiss();
                        try {
                            JSONArray array = response.getJSONArray("establishments");
                            establishmentsList.clear();

                            for(int i = 0; i < array.length(); i++){
                                JSONObject data = array.getJSONObject(i);
                                Establishment estb = new Establishment(data);
                                establishmentsList.add(estb);
                            }

                            if(establishmentsList.isEmpty()){
                                findViewById(R.id.establishmentList).setVisibility(View.INVISIBLE);
                                findViewById(R.id.listError).setVisibility(View.VISIBLE);
                            }else{
                                filterList(filter, establishmentsList);
                                sortList(sorter, establishmentsList);
                                mAdapter = new MyAdapter(establishmentsList, getSupportFragmentManager(), db);
                                recyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("result", "Ups, got an error: " + error.getLocalizedMessage());
                    }
                }
        ){
            @Override
            public Map getHeaders(){
                HashMap headers = new HashMap();
                headers.put("x-api-version", "2");
                headers.put("format", "JSON");

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(getRequest);
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDialogPositiveClick(Establishment estb) {
        establishmentsList.set(establishmentsList.indexOf(estb),estb);

        ArrayList<Establishment> toShow;

        toShow = filterList(filter, establishmentsList);
        sortList(sorter, toShow);
        mAdapter = new MyAdapter(toShow, getSupportFragmentManager(), db);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogPositiveClick(QueryType qr_type, Distance qr_distance, SortHolder sorter, FilterHolder filter) {
        Log.e("Filter Dialog", "Set new values from filter");
        queryBy = qr_type;
        queryDistance = qr_distance;
        this.sorter = sorter;
        this.filter = filter;

        ArrayList<Establishment> toShow;


        if(requestQuery != null){
            requestQuery.setUpdatedQuery(queryBy, "", queryDistance);
        }else{
            requestQuery = new QueryHolder(queryBy, "", queryDistance, lon, lat);
        }

        if(requestQuery.type.equals(QueryType.LOCATION)){
            loadRecyclerData(requestQuery);
            return;
        }


        toShow = filterList(filter, establishmentsList);
        sortList(sorter, toShow);
        mAdapter = new MyAdapter(toShow, getSupportFragmentManager(), db);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void sortList(SortHolder sorter, ArrayList<Establishment> data){
        switch (sorter.sortBy){
            case TYPE:
                data.sort(Establishment.COMPARE_BY_TYPE);
                break;
            case RATING:
                data.sort(Establishment.COMPARE_BY_RATING);
                break;
            case DISTANCE:
                data.sort(Establishment.COMPARE_BY_DISTANCE);
                break;
            case NAME:
                data.sort(Establishment.COMPARE_BY_NAME);
                Collections.reverse(data);
            case DATE:
                data.sort(Establishment.COMPARE_BY_DATE);
                break;
        }

        if(!sorter.ascending) Collections.reverse(data);
    }

    private ArrayList<Establishment> filterList(FilterHolder filter, ArrayList<Establishment> data){

        ArrayList<Establishment> filtered = new ArrayList<>();
        filtered.addAll(data);

        if(filter.isActive){
            for(Establishment pointer : data) {
                Log.e("Normal Establishment:", String.valueOf(pointer.distance));
                if(filter.removeNotRated){
                    int rating;

                    try{
                        rating = Integer.parseInt(pointer.rating);
                    }catch (NumberFormatException e){
                        filtered.remove(pointer);
                        continue;
                    }
                }


                if(!filter.businessType.equals(BusinessType.NULL)){
                    Log.e("BusinessType", String.valueOf(filter.businessType));
                   if(!pointer.businessType.equals(filter.businessType.name))
                       filtered.remove(pointer);
                        continue;
                }

                /*
                if(!filter.maxDistance.equals(Distance.NO_LIMIT)){
                    if(Double.parseDouble(pointer.distance) > filter.maxDistance.distance){
                        data.remove(i);
                        continue;
                    }
                }

                if(!filter.ratingOP.equals(FilterRating.NULL)){
                    int val;

                    try{
                        val = Integer.parseInt(pointer.rating);
                    }catch (NumberFormatException e){
                        val = -1;
                    }

                    switch (filter.ratingOP) {
                        case EQUAL:
                            if(val != filter.ratingVAL)
                                data.remove(i);
                            continue;
                        case GREATER:
                            if(val < filter.ratingVAL)
                                data.remove(i);
                            continue;
                        case SMALLER:
                            if(val > filter.ratingVAL)
                                data.remove(i);
                            continue;
                     }
                }
                */
            }

        }
        return filtered;
    }


    private void updateRecycler(){
        /*
        When do I want to update or display the recycler
        1. When I create App
        2. After I type in the thing
        3. After I exit from an EstablishmentView
        4. After I apply filter


         */

    }
}

