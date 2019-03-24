package com.mxn672.foodrating;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
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
import android.widget.Toast;

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
import com.mxn672.foodrating.data.Establishment;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private BottomNavigationView bottomNavi;
    private Intent intent;

    private ImageButton filterButton;
    private SearchView searchView;
    private ArrayList<Establishment> establishmentsList = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private double lon;
    private double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callPermissions();

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

        filterButton = findViewById(R.id.searchView_filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Action FILTER", Toast.LENGTH_SHORT).show();
                showAlertDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.establishmentList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(establishmentsList);
        recyclerView.setAdapter(mAdapter);

        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadRecyclerData(query);
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

    private void loadRecyclerData(String filters){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Data...");
        //progressDialog.show();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "http://api.ratings.food.gov.uk/establishments?name=" + filters +
                "&latitude=" + lat + "&longitude=" +  lon  + "&pageSize=20", null,
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

                            mAdapter = new MyAdapter(establishmentsList);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

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
}

