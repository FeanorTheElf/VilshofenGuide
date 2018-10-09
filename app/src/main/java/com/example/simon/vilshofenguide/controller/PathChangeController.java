package com.example.simon.vilshofenguide.controller;

import com.example.simon.vilshofenguide.sightseeing.SightManager;
import com.example.simon.vilshofenguide.sightseeing.pathfinder.Pathfinder;
import com.example.simon.vilshofenguide.sightseeing.Path;
import com.example.simon.vilshofenguide.sightseeing.Sight;
import com.example.simon.vilshofenguide.sightseeing.TripConfigurations;
import com.example.simon.vilshofenguide.view.PathShower;

import java.io.Serializable;

/**
 * Created by Simon on 09.03.2016.
 */
public class PathChangeController implements Serializable{

    private Path p;
    private transient PathShower pathShower;
    private TripConfigurations tg;

    public void itemDeleted(Sight s, SightManager manager) {
        tg.toggleSightExcluded(s);
        p = new Pathfinder(manager).calculatePath(tg);
        informPathShower();
    }

    public void itemDeleted(int numberInPath, SightManager manager) {
        this.itemDeleted(p.getSightAt(numberInPath), manager);
    }

    public void itemAdded(Sight s, SightManager manager) {
        tg.toggleSightIncluded(s);
        p = new Pathfinder(manager).calculatePath(tg);
        informPathShower();
    }

    public void calculateRoute(SightManager manager) {
        p = new Pathfinder(manager).calculatePath(tg);
        informPathShower();
    }

    public void setTripCofigurations(TripConfigurations tg){
        this.tg = tg;
    }

    public void registerPathShower(PathShower pathShower){
        this.pathShower = pathShower;
        informPathShower();
    }

    public PathChangeController(){
        p = new Path();
    }

    public void setPath(Path p){
        this.p = p;
        informPathShower();
    }

    private void informPathShower(){
        if (this.pathShower != null){
            this.pathShower.setPath(p);
        }
    }
}