package com.example.simon.vilshofenguide.sightseeing;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConfigurableSightManager implements SightManager, Serializable{

    private final Map<Integer, Sight> sights;
    private final List<NamedRoute> predefinedRoutes;
    private final Map<Sight, Map<Sight, Double>> distances;

    public static ConfigurableSightManager getEmptySightManager(){
        return new ConfigurableSightManager();
    }

    public List<NamedRoute> getPredefinedRoutes(){
        return this.predefinedRoutes;
    }

    public Sight getSightById(int id){
        return this.sights.get(id);
    }

    public Sight getSightByName(String stringValueOfSight){
        for (Sight s : this.getAllSights()){
            if (s.toString().equals(stringValueOfSight)){
                return s;
            }
        }
        return null;
    }

    public void addSight(Sight s){
        this.sights.put(s.getId(), s);
    }

    public void addRoute(NamedRoute r){
        this.predefinedRoutes.add(r);
    }

    public double getDistanceInMinutes(Sight sight1, Sight sight2) {
        if (sight1.equals(sight2)){
            return 0;
        }
        return this.distances.get(sight1).get(sight2);
    }

    public void setDistance(int id1, int id2, double distance){
        Sight sight1 = this.sights.get(id1);
        Sight sight2 = this.sights.get(id2);

        if (!this.distances.containsKey(sight1)) {
            this.distances.put(sight1, new HashMap<>());
        }
        if (!this.distances.containsKey(sight2)) {
            this.distances.put(sight2, new HashMap<>());
        }
        this.distances.get(sight1).put(sight2, distance);
        this.distances.get(sight2).put(sight1, distance);
    }

    public Sight getAnySight(){
        return this.sights.values().iterator().next();
    }

    public Collection<Sight> getAllSights() {
        return sights.values();
    }

    private ConfigurableSightManager(){
        sights = new HashMap<>();
        predefinedRoutes = new LinkedList<>();
        this.distances = new HashMap<>();
    }
}
