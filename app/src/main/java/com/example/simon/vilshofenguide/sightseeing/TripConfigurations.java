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
    private SightManager sightManager;

    private Set<Sight> excludedSights = new HashSet<>();
    private Set<Sight> includedSights = new HashSet<>();

    /**
     * Returns all Paths which originate from this one and are still possible
     * @param p
     * @return
     */
    @Deprecated
    public List<Path> getFurtherPossiblePaths(Path p) throws ExecutionException, InterruptedException {
        List<Path> result = new LinkedList<>();
        for (Path pathChild : p.getFurtherPaths(sightManager)){
            if (this.excludedSights.contains(pathChild.getLastSight())){
                continue;
            }
            int occurencesOfLastSight = pathChild.occurrencesOfSight(pathChild.getLastSight());
            //A sight must not be visited more often than one time, except its the start and end sight
            if (occurencesOfLastSight <= (pathChild.getLastSight().equals(startSight) && pathChild.getLastSight().equals(endSight) ? 2 : 1)){
                //TODO: if manually add sights is possible, test if the current length plus the length of all added sights is greater than the max length
                if (pathChild.getLenghtInMinutes(sightManager) <= this.getTotalTripMinutes()){
                    result.add(pathChild);
                }
            }
        }
        return result;
    }

    @Deprecated
    public boolean isLegalTargetPath(Path p){
        for (Sight s : this.includedSights){
            if (!p.asList().contains(s)){
                return false;
            }
        }
        return true;
    }

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

    public double getPathLengthInMinutes(Path p) throws ExecutionException, InterruptedException {
        return p.getLenghtInMinutes(this.sightManager);
    }

    /**
     * Returns the amount of points the path has reached until now
     * @param p
     * @return
     */
    public int getActualEvaluation(Path p){
        return p.getPoints(c);
    }

    public SightManager getSightManager(){
        return sightManager;
    }

    /**
     * Returns the estimated amount of points the best childpath of this path will reach more than
     * this path
     * @param p
     * @return
     */
    public int getEstimatedFuturousEvaluation(Path p) throws ExecutionException, InterruptedException {
        if(p.getLastSight().equals(this.getEndSight())){ // important to make Pathfinder work
            return 0;
        }
        return (int)Math.round(getTotalTripMinutes() * getActualEvaluation(p) / getPathLengthInMinutes(p));
    }

    public boolean sameStartEndSight(){
        return this.startSight.equals(this.endSight);
    }

    /**
     * Returns the estimated amount of points a childpath of this path will reach;
     * It can be assumed that this value mostly exceeds the exact one
     * @param p
     * @return
     */
    public int getEstimatedTotalEvaluation(Path p) throws ExecutionException, InterruptedException {
        return getActualEvaluation(p) + getEstimatedFuturousEvaluation(p);
    }

    public Sight getStartSight(){
        return this.startSight;
    }

    public Sight getEndSight(){
        return this.endSight;
    }

    public static TripConfigurations getDefaultTripConfigurations(Category c, int tripTime, SightManager sightManager){
        TripConfigurations tc = new TripConfigurations(sightManager);
        tc.c = c;
        tc.totalTripMinutes = tripTime;
        tc.startSight = sightManager.getDefaultSight();
        tc.endSight = sightManager.getDefaultSight();
        return tc;
    }

    public void includeSight(Sight s){
        if (this.excludedSights.contains(s)){
            this.excludedSights.remove(s);
        }
        this.includedSights.add(s);
    }

    public void excludeSight(Sight s){
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

    private TripConfigurations(SightManager sightManager){
        this.sightManager = sightManager;
    }
}
