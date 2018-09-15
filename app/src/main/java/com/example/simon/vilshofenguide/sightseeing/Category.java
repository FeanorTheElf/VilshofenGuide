package com.example.simon.vilshofenguide.sightseeing;

import android.content.Context;

import com.example.simon.vilshofenguide.R;

/**
 * Created by Simon on 03.01.2016.
 */
public enum Category {

    mixed(R.string.mixed), brewery(R.string.brewery), religiousBuildings(R.string.religiousBuildings), publicBuildings(R.string.publicBuildings);

    private final int id;

    Category(int id){
        this.id = id;
    }
}
