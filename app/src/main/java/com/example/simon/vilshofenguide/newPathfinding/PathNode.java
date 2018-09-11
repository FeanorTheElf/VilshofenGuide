package com.example.simon.vilshofenguide.newPathfinding;

import com.example.simon.vilshofenguide.pathfinding.Sight;
import com.example.simon.vilshofenguide.pathfinding.TripConfigurations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 29.11.2016.
 */

public class PathNode {

    private Sight[] path;
    private double lengthInMinutes = -1;
    private int points = -1;
    private int hashCode;
    private boolean hashCodeSet = false;
    private boolean containsIncludedSights;
    private boolean containsIncludedSightsIsSet = false;

    public boolean containsIncludedSights(TripConfigurations tc){
        if (containsIncludedSightsIsSet){
            return containsIncludedSights;
        }
        Set<Sight> leftSights = new HashSet<>(tc.getIncludedSights());
        for (Sight aPath : path) {
            leftSights.remove(aPath);
        }
        this.containsIncludedSights = leftSights.isEmpty();
        this.containsIncludedSightsIsSet = true;
        return this.containsIncludedSights;
    }

    public int hashCode(){
        if (!hashCodeSet){
            hashCode = 0;
            //Cantorsche Diagonalbijektion
            for (Sight s : path){
                int c = hashCode + s.getId();
                hashCode += ((c * (c + 1)) >> 1);
            }
            hashCodeSet = true;
        }
        return hashCode;
    }

    public boolean equals(Object that){
        if (that instanceof PathNode){
            if (that.hashCode() == this.hashCode()){
                return Arrays.deepEquals(this.path, ((PathNode)that).path);
            }
        }
        return false;
    }

    public double evaluate(TripConfigurations tc){
        if (tc.getEndSight().equals(this.getLastSight())){
            double[] params = tc.getSightManager().getParam(tc.getCategory());
            return 100 / (params[0] * getLengthInMinutes() + params[1]) * getPoints(tc);
        }
        double[] params = tc.getSightManager().getParam(tc.getCategory());
        return 100 / (params[0] * getLengthInMinutes() + params[1]) * getPoints(tc) * tc.getTotalTripMinutes() / lengthInMinutes;
//        return postprocess(tc.getTotalTripMinutes() * getPoints(tc) / lengthInMinutes);
    }

//    private double postprocess(double val){
//        return 100 / (-0.4 * getLengthInMinutes() + 72.81120046010983) * val;
//        return 100 / (-0.1219153591426935 * getLengthInMinutes() + 72.81120046010983) * val;
//    	return val;
//    	return 100 / (0.004689736678584432 * getLengthInMinutes() * getLengthInMinutes() - 0.5896015039019344 * getLengthInMinutes() + 82.45855228383657) * val;
//    }

    public List<PathNode> getNeighbours(Set<Sight> allSights, final TripConfigurations tc) throws ExecutionException, InterruptedException {
        if (this.getLastSight().equals(this.path[0]) && this.path.length > 1){
            return new LinkedList<>();
        }
        double timeLeft = tc.getTotalTripMinutes() - getLengthInMinutes();
        for (Sight aPath1 : path) {
            allSights.remove(aPath1);
        }
        if (tc.sameStartEndSight()){
            allSights.add(this.path[0]);
        }
        List<PathNode> result = new LinkedList<>();
        for (Sight s : allSights){
            double moreTime = tc.getSightManager().getDistanceInMinutes(path[path.length - 1], s) + s.getAverageVisitDurationInMinutes();
            if (moreTime <= timeLeft){
                Sight[] newPath = new Sight[this.path.length + 1];
                System.arraycopy(path, 0, newPath, 0, path.length);
                newPath[newPath.length - 1] = s;
                result.add(new PathNode(newPath, getLengthInMinutes() + moreTime));
            }
        }
        for (Sight aPath : path) {
            allSights.add(aPath);
        }
        Collections.sort(result, new Comparator<PathNode>() {
            @Override
            public int compare(PathNode o1, PathNode o2) {
                return -Double.compare(o1.evaluate(tc), o2.evaluate(tc));
            }
        });
        return result;
    }

    public Sight[] getPath(){
        return this.path;
    }

    public Sight getLastSight(){
        return this.path[this.path.length - 1];
    }

    public int getPoints(TripConfigurations tc){
        if (points == -1){
            int result = 0;
            for (Sight s : path){
                result += s.getPoints(tc.getCategory());
            }
            this.points = result;
        }
        return points;
    }

    public double getLengthInMinutes(){
        return lengthInMinutes;
    }

    public PathNode(Sight[] path, double lengthInMinutes){
        this.path = path;
        this.lengthInMinutes = lengthInMinutes;
    }
}
