package com.mxn672.foodrating.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.mxn672.foodrating.R;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavi;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavi = (BottomNavigationView) findViewById(R.id.bottom_navi);
        bottomNavi.setSelectedItemId(R.id.profile);
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
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent, options.toBundle());
                        break;

                    case R.id.profile:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
