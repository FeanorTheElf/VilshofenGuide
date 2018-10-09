package com.example.simon.vilshofenguide.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.controller.PathChangeController;
import com.example.simon.vilshofenguide.sightseeing.SightInitializer;
import com.example.simon.vilshofenguide.sightseeing.SightManager;

import java.util.concurrent.ExecutionException;

public class StartActivity extends AppCompatActivity{

    private SightManager manager;
    private AsyncTask<?, ?, SightManager> sightTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.manager = (SightManager)getIntent().getSerializableExtra("sightManager");
        if (this.manager == null){
            startSightManagerDownload();
        }
        setContentView(R.layout.activity_start);
    }

    public void changeToTripPlanActivity(View view){
        waitForManagerInitialized();
        Intent intentTripPlan = new Intent(this, TripPlanActivity.class);
        intentTripPlan.putExtra("path", new PathChangeController());
        intentTripPlan.putExtra("sightManager", manager);
        startActivity(intentTripPlan);
    }

    public void changeToVilshofenMap(View view){
        waitForManagerInitialized();
        Intent intent = new Intent(this, VilshofenMapActivity.class);
        intent.putExtra("sightManager", manager);
        startActivity(intent);
    }

    private void startSightManagerDownload(){
        SightInitializer s = new SightInitializer();
        try {
            sightTask = s.downloadSightManager();
        } catch (Exception e) {
            changeToErrorActivity(e);
        }
    }

    private void waitForManagerInitialized(){
        try {
            if (manager != null){
                return;
            }
            manager = sightTask.get();
            if (manager.getAllSights().isEmpty()){
                changeToErrorActivity(new Exception("Es konnte keine Verbindung zum Server hergestellt werden. Prüfen Sie Ihre Internet-Verbindung."));
            }
        } catch (InterruptedException | ExecutionException e) {
            changeToErrorActivity(e);
        }
    }

    private void changeToErrorActivity(Exception e){
        Intent intent = new Intent(this, ErrorActivity.class);
        intent.putExtra("exception", e);
        startActivity(intent);
    }

    public void changeToChooseSightActivity(View view){
        waitForManagerInitialized();
        Intent intent = new Intent(this, AllSightsActivity.class);
         intent.putExtra("sightManager", manager);
        startActivity(intent);
    }

    public void changeToCreditsActivity(View view){
        waitForManagerInitialized();
        Intent intent = new Intent(this,CreditsActivity.class);
        intent.putExtra("sightManager", manager);
        startActivity(intent);
    }
}
