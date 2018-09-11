package com.example.simon.vilshofenguide.com.example.simon.vilshofenguide.controller;

import com.example.simon.vilshofenguide.sightseeing.pathfinder.PathfinderNew;
import com.example.simon.vilshofenguide.sightseeing.Path;
import com.example.simon.vilshofenguide.sightseeing.Sight;
import com.example.simon.vilshofenguide.sightseeing.TripConfigurations;
import com.example.simon.vilshofenguide.view.PathShower;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 09.03.2016.
 */
public class PathChangeController implements Serializable{

    private Path p;
    private transient PathShower pathShower;
    private TripConfigurations tg;

    public void itemDeleted(Sight s) throws ExecutionException, InterruptedException {
        tg.excludeSight(s);
        p = new PathfinderNew().calculatePath(tg);
        informPathShower();
    }

    public void itemDeleted(int numberInPath) throws ExecutionException, InterruptedException {
        this.itemDeleted(p.getSightAt(numberInPath));
    }

    public void itemAdded(Sight s) throws ExecutionException, InterruptedException {
        tg.includeSight(s);
        p = new PathfinderNew().calculatePath(tg);
        informPathShower();
    }

    public void calculateRoute() throws ExecutionException, InterruptedException {
        p = new PathfinderNew().calculatePath(tg);
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