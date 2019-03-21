package com.mxn672.foodrating;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private BottomNavigationView bottomNavi;

    private ImageButton filterButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavi = (BottomNavigationView) findViewById(R.id.bottom_navi);
        bottomNavi.setSelectedItemId(R.id.search);
        bottomNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.map:
                        Toast.makeText(getApplicationContext(), "Action MAP", Toast.LENGTH_SHORT).show();
                        onRequestSearch();
                        Log.e("result", "I search for data");
                        break;

                    case R.id.search:
                        Toast.makeText(getApplicationContext(), "Action SEARCH", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.profile:
                    Toast.makeText(getApplicationContext(), "Action PROFILE", Toast.LENGTH_SHORT).show();
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

        String[] input = new String[15];
        for (int i = 0; i < 15; i++) {
            input[i] = "Restaurant " + (i+1);
        }

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(input);
        recyclerView.setAdapter(mAdapter);
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog alertDialog = new FilterDialog();
        alertDialog.show(fm, "filterDialog");
    }


    public void onRequestSearch() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(RequestHandler.setByName("Roosters"));
    }


}
