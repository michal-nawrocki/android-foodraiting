package com.mxn672.foodrating.activities;

import android.app.ActivityOptions;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.mxn672.foodrating.R;
import com.mxn672.foodrating.data.Establishment;
import com.mxn672.foodrating.data.EstablishmentDatabase;
import com.mxn672.foodrating.fragments.EstablishmentFragment;
import com.mxn672.foodrating.recyclerView.MyAdapter;
import com.mxn672.foodrating.recyclerView.SimpleDividerItemDecoration;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements EstablishmentFragment.NoticeDialogListener {

    private BottomNavigationView bottomNavi;
    private Intent intent;
    private ImageButton aboutButton;
    private ImageButton settingsButton;
    public RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter mAdapter;
    private ArrayList<Establishment> favouriteList;
    private EstablishmentDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Databse setup
        db = Room.databaseBuilder(getApplicationContext(), EstablishmentDatabase .class, "estb-database").allowMainThreadQueries().build();

        // Bottom Navigation setup
        bottomNavi = (BottomNavigationView) findViewById(R.id.profile_navi);

        // Animation Transition
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(this, bottomNavi, "bottom_navi");;

        bottomNavi.setSelectedItemId(R.id.profile);
        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.map:
                        intent = new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.search:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.profile:
                        break;
                }
                return true;
            }
        });


        // Listeners for About and Settings Fragments
        aboutButton = findViewById(R.id.profile_about);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display dev details dialog
            }
        });

        settingsButton = findViewById(R.id.profile_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display settings dialog
            }
        });

        // RecyclerView setup
        recyclerView = findViewById(R.id.profile_recycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        // get data adn setup adapter
        getFavouriteData();

    }


    private void getFavouriteData(){

        if(!db.establishmentDao().getAll().isEmpty()){
            findViewById(R.id.profile_recycler).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_error).setVisibility(View.INVISIBLE);
            favouriteList = (ArrayList) db.establishmentDao().getAll();

            mAdapter = new MyAdapter(favouriteList, getSupportFragmentManager(), db, true);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else{
            findViewById(R.id.profile_recycler).setVisibility(View.INVISIBLE);
            findViewById(R.id.profile_error).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDialogPositiveClick(Establishment estb) {


        if(db.establishmentDao().getAll().isEmpty()){
            findViewById(R.id.profile_recycler).setVisibility(View.INVISIBLE);
            findViewById(R.id.profile_error).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.profile_recycler).setVisibility(View.VISIBLE);
            findViewById(R.id.profile_error).setVisibility(View.INVISIBLE);
            mAdapter = new MyAdapter((ArrayList) db.establishmentDao().getAll(), getSupportFragmentManager(), db, true);
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }
}
