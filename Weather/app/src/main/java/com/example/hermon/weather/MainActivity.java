package com.example.hermon.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.example.hermon.weather.adapter.CityRecyclerAdapter;
import com.example.hermon.weather.touch.CityTouchHelperCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CITY_NAME = "city name";

    private CityRecyclerAdapter cityRecyclerAdapter;
    private RecyclerView recyclerCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication)getApplication()).openRealm();

        setupRecyclerView();
        setupAddCItyUI();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupAddCItyUI() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addCity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCityDialog();
                //Toast.makeText(MainActivity.this,"Clicked add city fab",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addCityDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add City");

        final EditText cityName = new EditText(this);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cityRecyclerAdapter.addCity(cityName.getText().toString().trim());
                recyclerCities.scrollToPosition(0);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.setCancelable(true).setView(cityName).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if(TextUtils.isEmpty(cityName.getText().toString()))
                    ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        cityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = s.toString();
                str = str.trim();
                if (str.isEmpty()){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                str = str.trim();
                if (str.isEmpty()){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                str = str.trim();
                if (str.isEmpty()){
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(false);
                }
                else{
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(true);
                }
            }
        });
        dialog.show();
    }

    private void setupRecyclerView() {
        recyclerCities = (RecyclerView) findViewById(R.id.recyclerCity);
        recyclerCities.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerCities.setLayoutManager(layoutManager);

        cityRecyclerAdapter = new CityRecyclerAdapter(this,
                ((MainApplication)getApplication()).getRealmCities());

        recyclerCities.setAdapter(cityRecyclerAdapter);

        ItemTouchHelper.Callback callback = new CityTouchHelperCallback(cityRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerCities);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deleteAll) {
            cityRecyclerAdapter.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addCity) {
            addCityDialog();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this,"This App was made by Hermon Mulat",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showWeatherDetails(String cityName){
        Intent showWeatherDetails =  new Intent(this,ShowCityWeather.class);
        showWeatherDetails.putExtra(CITY_NAME,cityName);
        startActivity(showWeatherDetails);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication)getApplication()).closeRealm();
    }
}
