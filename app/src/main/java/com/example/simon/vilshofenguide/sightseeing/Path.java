package com.example.simon.vilshofenguide.sightseeing;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 27.01.2016.
 */
public class Path implements Cloneable, Serializable, Iterable<Sight>{

    private List<Sight> path;

    public double getLenghtInMinutes(SightManager sightManager) throws ExecutionException, InterruptedException {
        double result = 0;
        Sight last = path.get(0);
        //The difference between the first and the first element ist added, too (it is 0)
        for (Sight s : path) {
            result += sightManager.getDistanceInMinutes(s, last);
            result += s.getAverageVisitDurationInMinutes();
            last = s;
        }
        return result;
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Sight s : this.path){
            result.append(s.toString()).append(", ");
        }
        return result.toString();
    }

    public boolean equals(Object that){
        if (that.hashCode() != this.hashCode()){
            return false;
        }
        if (that instanceof Path){
            return ((Path)that).path.equals(this.path);
        }
        return false;
    }

    public List<Sight> asList(){
        return path;
    }

    public Sight getSightAt(int i){
        return this.path.get(i);
    }

    public Sight getLastSight(){
        return this.getSightAt(this.path.size() - 1);
    }

    public Path clone(){
        try {
            Path result = (Path)super.clone();
            result.path = new LinkedList<>(this.path);
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the sum of points of all sights this path contains in the specified category
     * @param c
     * @return
     */
    public int getPoints(Category c){
        int result = 0;
        for (Sight s : path){
            result += s.getPoints(c);
        }
        if (this.getLastSight().equals(this.path.get(0))) {
            result -= this.path.get(0).getPoints(c);
        }
        return result;
    }

    /**
     * Returns all paths that originate from this path if a sight is appended to this path
     * @return
     */
    public List<Path> getFurtherPaths(SightManager sightManager){
        List<Path> result = new LinkedList<>();
        List<Sight> possibleSights = new LinkedList<>(sightManager.getAllSights());
        for (Sight s : possibleSights){
            Path p = this.clone();
            p.path.add(s);
            result.add(p);
        }
        return result;
    }

    public int occurrencesOfSight(Sight s){
        return Collections.frequency(this.path, s);
    }

    public int getSightNumber(){
        return this.path.size();
    }

    public Path(Collection<? extends Sight> path){

        this.path = new LinkedList<>(path);
    }

    private void addSightsArray(Sight[] sights){
        for (Sight s : sights){
            this.path.add(s);
        }
    }

    public Path(Sight... path){
        this();
        this.addSightsArray(path);
    }

    public Path(Sight start){
        this();
        this.path.add(start);
    }

    public Path(){
        this(new LinkedList<>());
    }

    public Path(List<Sight> path){
        this.path = path;
    }

    @Override
    public Iterator<Sight> iterator() {
        return this.path.iterator();
    }
}
