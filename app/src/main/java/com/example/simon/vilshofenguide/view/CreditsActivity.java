package com.example.simon.vilshofenguide.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.pathfinding.PathChangeController;
import com.example.simon.vilshofenguide.pathfinding.SightManager;

public class CreditsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SightManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.manager = (SightManager)getIntent().getSerializableExtra("sightManager");

        setContentView(R.layout.activity_impressum);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent myIntent = new Intent(this, StartActivity.class);
            startActivity(myIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.impressum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeToSightList(){
        Intent intent = new Intent(this, AllSightsActivity.class);
        intent.putExtra("sightManager", this.manager);
        startActivity(intent);
    }

    public void changeToChoosePredefinedRoute(){
        Intent intent = new Intent(this, ChoosePredefinedRouteActivity.class);
        intent.putExtra("path", new PathChangeController());
        intent.putExtra("sightManager", this.manager);
        startActivity(intent);
    }

    public void changeToStartActivity(){
        Intent intent = new Intent(this, StartActivity.class);
        intent.putExtra("sightManager", this.manager);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_routes) {
            changeToChoosePredefinedRoute();
        }else if (id==R.id.nav_sights){
            changeToSightList();
        }else if (id==R.id.back){
            changeToStartActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

