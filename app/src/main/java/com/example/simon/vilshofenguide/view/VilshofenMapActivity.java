package com.example.simon.vilshofenguide.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.sightseeing.GoogleMapsDirectionsClient;
import com.example.simon.vilshofenguide.sightseeing.Path;
import com.example.simon.vilshofenguide.com.example.simon.vilshofenguide.controller.PathChangeController;
import com.example.simon.vilshofenguide.sightseeing.Sight;
import com.example.simon.vilshofenguide.sightseeing.SightManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class VilshofenMapActivity extends FragmentActivity implements OnMapReadyCallback, PathShower, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private PathChangeController pathChanger;
    private SightManager manager;
    private Sight lastClicked;
    private GoogleMapsDirectionsClient googleMapsPathfinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vilshofen_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent().getSerializableExtra("path") != null){
            this.pathChanger = (PathChangeController)getIntent().getSerializableExtra("path");
        }
        this.manager = (SightManager)getIntent().getSerializableExtra("sightManager");
        this.googleMapsPathfinder = new GoogleMapsDirectionsClient();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng vilshofen = new LatLng(48.633779, 13.187136);
        float zoomLevel = 15.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vilshofen, zoomLevel));

        if (this.pathChanger != null){
            this.pathChanger.registerPathShower(this);
        }else{
            createPathMarkers(null);
        }
    }

    private void createPathMarkers(Path p) {
        for (Sight s : (p != null ? p.asList() : this.manager.getAllSights())){
            mMap.addMarker(new MarkerOptions().position(new LatLng(s.getDegreeOfLatitude(), s.getDegreeOfLongitude())).title(s.toString()));
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void setPath(Path p) {
        PolylineOptions po = new PolylineOptions();
        for (int i = 1; i < p.getSightNumber(); i++){
            this.googleMapsPathfinder.setOrigin(new LatLng(p.getSightAt(i - 1).getDegreeOfLatitude(), p.getSightAt(i - 1).getDegreeOfLongitude()));
            this.googleMapsPathfinder.setDestination(new LatLng(p.getSightAt(i).getDegreeOfLatitude(), p.getSightAt(i).getDegreeOfLongitude()));
            for (LatLng lng : this.googleMapsPathfinder.calculatePath()){
                po.add(lng);
            }
        }
        mMap.clear();
        this.createPathMarkers(p);
        mMap.addPolyline(po.width(5).color(Color.RED));
    }

    private void changeToSightDetailActivity(Sight s){
        Intent intent = new Intent(this, SightDetailActivity.class);
        intent.putExtra("path", this.pathChanger);
        intent.putExtra("sightManager", this.manager);
        intent.putExtra("sight", s);
        startActivity(intent);
    }

    public boolean onMarkerClick(Marker m){
        Sight s = manager.getSightByName(m.getTitle());
        if (lastClicked != null && s.equals(lastClicked)){
            changeToSightDetailActivity(s);
        }
        lastClicked = s;
        return false;
    }
}
