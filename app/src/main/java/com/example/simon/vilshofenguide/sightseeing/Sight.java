package com.example.simon.vilshofenguide.sightseeing;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Simon on 03.01.2016.
 */
public class Sight implements Serializable{

    public static final boolean useEnglish = false;

    private final int id;
    private final Map<Category, Integer> points;
    private final String htmlFileName;
    private final double degreeOfLatitude;
    private final double degreeOfLongitude;
    private final String englishName;
    private final String germanName;
    private final double averageVisitDurationInMinutes;

    public boolean equals(Object that) {
        return that instanceof Sight && ((Sight) that).id == this.id;
    }

    public int hashCode(){
        return getId();
    }

    public int getId(){
        return id;
    }

    public double getAverageVisitDurationInMinutes(){
        return averageVisitDurationInMinutes;
    }

    public String toString(){
        return useEnglish?englishName:germanName;
    }

    public String getHtmlFileName(){
        return this.htmlFileName;
    }

    public double getDegreeOfLatitude(){
        return degreeOfLatitude;
    }

    public double getDegreeOfLongitude(){
        return degreeOfLongitude;
    }

    public int getPoints(Category c){
        if (c == Category.mixed && !this.points.containsKey(Category.mixed)){
            int maxPoints = 0;
            for (Map.Entry<Category, Integer> categoryIntegerEntry : this.points.entrySet()){
                if (categoryIntegerEntry.getValue() > maxPoints){
                    maxPoints = categoryIntegerEntry.getValue();
                }
            }
            this.points.put(Category.mixed, maxPoints);
            return maxPoints;
        }else if (!this.points.containsKey(c)){
            return 0;
        }
        return this.points.get(c);
    }

    public Sight(int id, Map<Category, Integer> points, double degreeOfLatitude, double degreeOfLongitude, String englishName, String germanName, String htmlFileName, double averageVisitDurationInMinutes){
        this.id = id;
        this.points = points;
        this.htmlFileName = htmlFileName;
        this.degreeOfLatitude = degreeOfLatitude;
        this.averageVisitDurationInMinutes = averageVisitDurationInMinutes;
        this.degreeOfLongitude = degreeOfLongitude;
        this.englishName = englishName;
        this.germanName = germanName;
    }
}
