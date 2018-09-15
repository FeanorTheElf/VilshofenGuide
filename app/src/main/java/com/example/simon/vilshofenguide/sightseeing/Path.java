package com.example.simon.vilshofenguide.sightseeing;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Simon on 27.01.2016.
 */
public class Path implements Cloneable, Serializable, Iterable<Sight>{

    private List<Sight> path;

    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Sight s : this.path){
            result.append(s.toString()).append(", ");
        }
        return result.toString();
    }

    public boolean equals(Object that) {
        if (that.hashCode() != this.hashCode()) {
            return false;
        }
        return that instanceof Path && ((Path) that).path.equals(this.path);
    }

    public List<Sight> asList(){
        return path;
    }

    public Sight getSightAt(int i){
        return this.path.get(i);
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

    public int getSightNumber(){
        return this.path.size();
    }

    private void addSightsArray(Sight[] sights){
        Collections.addAll(this.path, sights);
    }

    public Path(){
        this(new LinkedList<>());
    }

    public Path(List<Sight> path){
        this.path = path;
    }

    @NonNull
    @Override
    public Iterator<Sight> iterator() {
        return this.path.iterator();
    }
}
