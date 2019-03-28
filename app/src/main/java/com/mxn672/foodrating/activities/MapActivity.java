package com.mxn672.foodrating.activities;

import android.Manifest;
import android.app.ActivityOptions;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.Distance;
import com.mxn672.foodrating.data.Establishment;
import com.mxn672.foodrating.data.EstablishmentDatabase;
import com.mxn672.foodrating.data.QueryHolder;
import com.mxn672.foodrating.data.QueryType;
import com.mxn672.foodrating.fragments.EstablishmentFragment;
import com.mxn672.foodrating.fragments.interfaces.EstablishmentDialogListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, EstablishmentDialogListener {

    private BottomNavigationView bottomNavi;
    private Intent intent;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private EstablishmentDatabase db;

    private double lat;
    private double lon;

    private QueryHolder mapQueryTake;
    private QueryHolder mapQueryRest;

    private ArrayList<Establishment> allDataAround = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get Databse setup
        db = Room.databaseBuilder(getApplicationContext(), EstablishmentDatabase .class, "estb-database").allowMainThreadQueries().build();


        bottomNavi = (BottomNavigationView) findViewById(R.id.bottom_navi);
        bottomNavi.setSelectedItemId(R.id.map);
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, bottomNavi, "bottom_navi");
        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.map:
                        break;

                    case R.id.search:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.profile:
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent, options.toBundle());
                        break;
                }
                return true;
            }
        });

        callPermissions();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton button = findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoreData();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Establishment position = (Establishment) marker.getTag();

                if(position != null){
                    FragmentManager fm = getSupportFragmentManager();
                    EstablishmentFragment alertDialog = new EstablishmentFragment(position, db);
                    alertDialog.show(fm, "esablishment");
                }

                return true;
            }
        });
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

                    mapQueryTake = new QueryHolder(QueryType.LOCATION, "", Distance.ONE_MILE, lon, lat, 7844);
                    mapQueryRest = new QueryHolder(QueryType.LOCATION, "", Distance.ONE_MILE, lon, lat, 1);

                    loadRecyclerData(mapQueryTake, true);


                }
            }, getMainLooper());
        }else{
            callPermissions();
        }
    }

    public void loadRecyclerData(QueryHolder qr, boolean overload){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, qr.getQueryURL(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressDialog.dismiss();
                        try {
                            JSONArray array = response.getJSONArray("establishments");
                            allDataAround.clear();

                            for(int i = 0; i < array.length(); i++){
                                JSONObject data = array.getJSONObject(i);
                                Establishment estb = new Establishment(data);
                                allDataAround.add(estb);
                                Log.e("Name", estb.businessName);

                                double thisLat = 0;
                                double thisLon = 0;
                                LatLng gps;

                                try{
                                    thisLat = Double.parseDouble(estb.lat);
                                    thisLon = Double.parseDouble(estb.lon);

                                }catch(NullPointerException e){
                                    continue;
                                }catch(NumberFormatException e){
                                    continue;
                                }

                                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(thisLat, thisLon)).title(estb.businessName));
                                marker.setTag(estb);

                            }

                            if(overload){
                                loadRecyclerData(mapQueryRest, false);
                            }

                            populateMap();

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

    private void populateMap(){

        LatLng location = new LatLng(lat,lon);
        Marker mark = mMap.addMarker(new MarkerOptions().position(location).title("Your location").icon(getMarkerIcon("#ff2299")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        mark.setTag(null);

    }

    private void addMoreData(){
        mapQueryRest.setNextPage();
        mapQueryTake.setNextPage();
        loadRecyclerData(mapQueryTake, true);
    }

    // method definition
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }


    @Override
    public void onDialogPositiveClick(Establishment estb) {
    }
}
