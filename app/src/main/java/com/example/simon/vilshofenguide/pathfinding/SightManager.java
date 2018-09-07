package com.example.simon.vilshofenguide.pathfinding;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 03.01.2016.
 */
public class SightManager implements Serializable{

    private Map<Integer, Sight> sights;
    private List<NamedRoute> predefinedRoutes;
    private Map<Sight, Map<Sight, Double>> distances;
    private Map<Category, double[]> params = new HashMap<>();
    /**
     * For test purposes
     */
    private boolean use1AsDistance = false;

    public static SightManager getEmptySightManager(){
        SightManager result = new SightManager();
        return result;
    }

    public static SightManager getTestSightManager(){
        SightManager result = new SightManager();
        result.initTestSights();
        return result;
    }

    public List<NamedRoute> getPredefinedRoutes(){
        return this.predefinedRoutes;
    }

    public Sight getSightById(int id){
        return this.sights.get(id);
    }

    public void useOneAsDistance(boolean value){
        this.use1AsDistance = value;
    }

    public Sight getSightWithName(String stringValueOfSight){
        for (Sight s : this.getAllSights()){
            if (s.toString().equals(stringValueOfSight)){
                return s;
            }
        }
        return null;
    }

    private void initTestSights(){
        Map<Category, Integer> pointsTestsight1 = new HashMap<>();
        pointsTestsight1.put(Category.brewery, 4);
        sights.put(1, new Sight(1, pointsTestsight1, 48.634, 13.186, "LocalTest1EN", "LocalTest1DE", "test", 5));

        Map<Category, Integer> pointsTestsight2 = new HashMap<>();
        pointsTestsight2.put(Category.religiousBuildings, 3);
        sights.put(2, new Sight(2, pointsTestsight2, 48.632, 13.187, "LocalTest2EN", "LocalTest2DE", "test", 5));

        Map<Category, Integer> pointsTestsight3 = new HashMap<>();
        pointsTestsight3.put(Category.brewery, 3);
        pointsTestsight3.put(Category.religiousBuildings, 1);
        pointsTestsight3.put(Category.publicBuildings, 2);
        sights.put(3, new Sight(3, pointsTestsight3, 48.634, 13.184, "LocalTest3EN", "LocalTest3DE", "test", 5));

        this.predefinedRoutes.add(new NamedRoute(new Path(this.sights.values()), "testrouteEN", "testrouteDE"));
    }

    public void addSight(Sight s){
        this.sights.put(s.getId(), s);
    }

    public void addRoute(NamedRoute r){
        this.predefinedRoutes.add(r);
    }

    public double getDistanceInMinutes(Sight sight1, Sight sight2) throws ExecutionException, InterruptedException {
        if (sight1.equals(sight2)){
            return 0;
        }
        return this.distances.get(sight1).get(sight2);
    }

    public void setDistance(int id1, int id2, double distance){
        Sight sight1 = this.sights.get(id1);
        Sight sight2 = this.sights.get(id2);

        if (!this.distances.containsKey(sight1)) {
            this.distances.put(sight1, new HashMap<Sight, Double>());
        }
        if (!this.distances.containsKey(sight2)) {
            this.distances.put(sight2, new HashMap<Sight, Double>());
        }
        this.distances.get(sight1).put(sight2, distance);
        this.distances.get(sight2).put(sight1, distance);
    }

    public Sight getDefaultSight(){
        return this.sights.values().iterator().next();
    }

    public Collection<Sight> getAllSights() {
        return sights.values();
    }

    public List<Sight> getAllSightsAsList(){
        return new LinkedList<>(getAllSights());
    }

    public void setParam(Category c, double[] val){
        this.params.put(c, val);
    }

    public double[] getParam(Category c){
        return this.params.get(c);
    }

    private SightManager(){
        sights = new HashMap<>();
        predefinedRoutes = new LinkedList<>();
        this.distances = new HashMap<>();
    }
}
