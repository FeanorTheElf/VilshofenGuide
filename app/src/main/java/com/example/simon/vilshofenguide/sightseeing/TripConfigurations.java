package com.example.simon.vilshofenguide.sightseeing;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 17.02.2016.
 */
public class TripConfigurations implements Serializable{

    private Category c;
    private int totalTripMinutes;
    private Sight startSight;
    private Sight endSight;

    private Set<Sight> excludedSights = new HashSet<>();
    private Set<Sight> includedSights = new HashSet<>();

    public Set<Sight> getIncludedSights(){ return this.includedSights; }

    public Category getCategory(){
        return this.c;
    }

    public Set<Sight> getExcludedSights(){
        return this.excludedSights;
    }

    /**
     * Returns the number of minutes the trip can last
     * @return
     */
    public int getTotalTripMinutes(){
        return totalTripMinutes;
    }

    public Sight getStartSight(){
        return this.startSight;
    }

    public Sight getEndSight(){
        return this.endSight;
    }

    public static TripConfigurations getDefaultTripConfigurations(Category c, int tripTime, Sight start, Sight end){
        TripConfigurations tc = new TripConfigurations();
        tc.c = c;
        tc.totalTripMinutes = tripTime;
        tc.startSight = start;
        tc.endSight = end;
        return tc;
    }

    public void toggleSightIncluded(Sight s){
        if (this.excludedSights.contains(s)){
            this.excludedSights.remove(s);
        }
        this.includedSights.add(s);
    }

    public void toggleSightExcluded(Sight s){
        if (this.includedSights.contains(s)){
            this.includedSights.remove(s);
        }
        this.excludedSights.add(s);
    }

    public void setCategory(Category c){
        this.c = c;
    }

    public void setStartSight(Sight s){
        this.startSight = s;
    }

    public void setEndSight(Sight s){
        this.endSight = s;
    }

    private TripConfigurations() {

    }
}
