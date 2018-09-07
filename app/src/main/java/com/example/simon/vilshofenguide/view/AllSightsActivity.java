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
import com.example.simon.vilshofenguide.pathfinding.Category;
import com.example.simon.vilshofenguide.pathfinding.Sight;
import com.example.simon.vilshofenguide.pathfinding.SightManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AllSightsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private SightManager manager;
    private ListView allSights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightlist);

        this.manager = (SightManager)getIntent().getSerializableExtra("sightManager");

        List<Sight> sightList = new LinkedList<>(manager.getAllSights());
        sightList.add(new Sight(100001, new HashMap<Category, Integer>(), 0, 0, "", "", "", 0));
        sightList.add(new Sight(100002, new HashMap<Category, Integer>(), 0, 0, "", "", "", 0));
        allSights = (ListView) findViewById(R.id.all_sights_list);
        ListAdapter listAdapter = new ArrayAdapter<Sight>(this, android.R.layout.simple_list_item_1, sightList);
        this.allSights.setAdapter(listAdapter);

        allSights.setOnItemClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (((Sight)allSights.getItemAtPosition(position)).getHtmlFileName().equals("")){
            return;
        }
        Intent intent = new Intent(this, SightDetailActivity.class);
        intent.putExtra("sightManager", this.manager);
        intent.putExtra("sight", (Sight)allSights.getItemAtPosition(position));
        startActivity(intent);
    }
}
