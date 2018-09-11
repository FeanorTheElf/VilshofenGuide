package com.example.simon.vilshofenguide.pathfinding;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.view.StringRessourceManager;

/**
 * Created by Simon on 03.01.2016.
 */
public enum Category {

    mixed(R.string.mixed), brewery(R.string.brewery), religiousBuildings(R.string.religiousBuildings), publicBuildings(R.string.publicBuildings);

    private int id;

    public String toString(){
        return StringRessourceManager.getInstance().getString(id);
    }

    Category(int id){
        this.id = id;
    }
}
