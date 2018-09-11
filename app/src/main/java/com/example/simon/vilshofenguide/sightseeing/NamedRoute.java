package com.example.simon.vilshofenguide.sightseeing;

import java.io.Serializable;

/**
 * Created by Simon on 01.07.2016.
 */
public class NamedRoute implements Serializable{

    private Path p;
    private String englishName;
    private String germanName;

    public String toString(){
        return Sight.useEnglish?englishName:germanName;
    }

    public Path getPath(){
        return p;
    }

    public NamedRoute(Path p, String englishName, String germanName){
        this.p = p;
        this.germanName = germanName;
        this.englishName = englishName;
    }

    public NamedRoute(){
        this(new Path(), "empty route", "leere route");
    }
}
