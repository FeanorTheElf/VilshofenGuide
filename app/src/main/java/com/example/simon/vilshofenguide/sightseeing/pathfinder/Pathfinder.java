package com.example.simon.vilshofenguide.sightseeing.pathfinder;

import com.example.simon.vilshofenguide.sightseeing.Category;
import com.example.simon.vilshofenguide.sightseeing.Path;
import com.example.simon.vilshofenguide.sightseeing.Sight;
import com.example.simon.vilshofenguide.sightseeing.SightManager;
import com.example.simon.vilshofenguide.sightseeing.TripConfigurations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 29.11.2016.
 */
public class Pathfinder {

    private SightManager manager;

    private double calcPathDistance(List<Sight> path){
        Iterator<Sight> it = path.iterator();
        Sight current = it.next();
        double sum = current.getAverageVisitDurationInMinutes();
        while (it.hasNext()){
            Sight next = it.next();
            sum += manager.getDistanceInMinutes(current, next);
            current = next;
            sum += current.getAverageVisitDurationInMinutes();
        }
        return sum;
    }

    private int calcOptimalInsertBeforeIndex(List<Sight> path, final Sight newSight){
        double currentMinDist = Double.POSITIVE_INFINITY;
        int currentMinIndex = -1;
        for (int i = 1; i < path.size(); ++i){
            path.add(i, newSight);
            if (calcPathDistance(path) < currentMinDist){
                currentMinDist = calcPathDistance(path);
                currentMinIndex = i;
            }
            path.remove(i);
        }
        return currentMinIndex;
    }

    private double valueToAdditionalDistanceRatio(Sight main, Sight additional, Category c){
        return additional.getPoints(c) / (manager.getDistanceInMinutes(main, additional) + additional.getAverageVisitDurationInMinutes());
    }

    private double calcNodeEvaluation(final Sight node, final double maxDistance, Map<Sight, Double> distsToPath, Category c){
        List<Sight> allSights = new LinkedList<>(manager.getAllSights());
        for (Iterator<Sight> it = allSights.iterator(); it.hasNext();){
            if (distsToPath.get(it.next()) == 0)
                it.remove();
        }
        Collections.sort(allSights, (a, b) -> Double.compare(valueToAdditionalDistanceRatio(node, b, c), valueToAdditionalDistanceRatio(node, a, c)));
        int points = node.getPoints(c);
        double distance = distsToPath.get(node);

        for (Sight s : allSights){
            if (distance + manager.getDistanceInMinutes(node, s) + s.getAverageVisitDurationInMinutes() > maxDistance)
                return points / distance;
            else if ((points + s.getPoints(c)) / (distance + manager.getDistanceInMinutes(node, s) + s.getAverageVisitDurationInMinutes()) > (points / distance)){
                points += s.getPoints(c);
                distance += manager.getDistanceInMinutes(node, s) + s.getAverageVisitDurationInMinutes();
            }else{
                return points / distance;
            }
        }
        return points / distance;
    }

    private Sight findNodeWithMaxEvaluation(final double maxDist, List<Sight> path, double pathDist, Map<Sight, Double> distsToPath, Category c) {
        double currentMaxEval = Double.NEGATIVE_INFINITY;
        Sight result = null;
        for (Sight node : manager.getAllSights()){
            if (!path.contains(node) && pathDist + distsToPath.get(node) + node.getAverageVisitDurationInMinutes() < maxDist){
                double eval = calcNodeEvaluation(node, maxDist - pathDist, distsToPath, c);
                if (eval > currentMaxEval){
                    result = node;
                    currentMaxEval = eval;
                }
            }
        }
        return result;
    }

    private void calcNodeInsertionData(List<Sight> result, double pathDist,
                                       Map<Sight, Integer> bestInsertBeforeIndices, Map<Sight, Double> distsToPath, Category c) {
        for (Sight node : manager.getAllSights()){
            if (result.contains(node)){
                bestInsertBeforeIndices.put(node, -1);
                distsToPath.put(node, 0.);
            }else{
                int i = calcOptimalInsertBeforeIndex(result, node);
                bestInsertBeforeIndices.put(node, i);
                result.add(i, node);
                distsToPath.put(node, calcPathDistance(result) - pathDist);
                result.remove(i);
            }
        }
    }

    public Path calculatePath(TripConfigurations tc) {
        List<Sight> result = new LinkedList<>();
        result.add(tc.getStartSight());
        result.add(tc.getEndSight());

        while(true){
            double pathDist = calcPathDistance(result);
            Map<Sight, Integer> bestInsertBeforeIndices = new HashMap<>();
            Map<Sight, Double> distancesToPath = new HashMap<>();
            calcNodeInsertionData(result, pathDist, bestInsertBeforeIndices, distancesToPath, tc.getCategory());

            Sight newSight = findNodeWithMaxEvaluation(tc.getTotalTripMinutes(), result, pathDist, distancesToPath, tc.getCategory());
            if (newSight == null)
                return new Path(result);
            result.add(bestInsertBeforeIndices.get(newSight), newSight);
        }
    }

    public Pathfinder(SightManager manager){
        this.manager = manager;
    }
}
