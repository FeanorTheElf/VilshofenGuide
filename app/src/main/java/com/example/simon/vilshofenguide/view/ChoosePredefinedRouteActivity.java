package com.example.simon.vilshofenguide.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.pathfinding.NamedRoute;
import com.example.simon.vilshofenguide.pathfinding.PathChangeController;
import com.example.simon.vilshofenguide.pathfinding.SightManager;

import java.util.List;

public class ChoosePredefinedRouteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SightManager manager;
    private PathChangeController pathChanger;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_predefined_route);
        this.manager = (SightManager)getIntent().getSerializableExtra("sightManager");
        this.pathChanger = (PathChangeController)getIntent().getSerializableExtra("path");
        list = (ListView)findViewById(R.id.predefined_routes);
        initList();
        list.setOnItemClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private List<NamedRoute> getPredefinedRoutes(){
        return manager.getPredefinedRoutes();
    }

    private void initList(){
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getPredefinedRoutes());
        this.list.setAdapter(listAdapter);
    }

    public void changeToMapActivity(){
        Intent intent = new Intent(this, VilshofenMapActivity.class);
        intent.putExtra("path", this.pathChanger);
        intent.putExtra("sightManager", this.manager);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.pathChanger.setPath(((NamedRoute)list.getItemAtPosition(position)).getPath());
        changeToMapActivity();
    }
}
