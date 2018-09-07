package com.example.simon.vilshofenguide.newPathfinding;

import com.example.simon.vilshofenguide.pathfinding.Path;
import com.example.simon.vilshofenguide.pathfinding.Sight;
import com.example.simon.vilshofenguide.pathfinding.TripConfigurations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 29.11.2016.
 */

public class PathfinderNew {

    public Path calculatePath(TripConfigurations tc) throws ExecutionException, InterruptedException {
        PathfinderPathNodePriorityQueue open = new PathfinderPathNodePriorityQueue(tc);
        open.offer(new PathNode(new Sight[]{tc.getStartSight()}, tc.getStartSight().getAverageVisitDurationInMinutes()));

        Map<PathNode, PathNode> closed = new HashMap<>();

        Set<Sight> allSights = new HashSet<>();
        allSights.addAll(tc.getSightManager().getAllSights());
        allSights.removeAll(tc.getExcludedSights());

        PathNode current;
        while (true){
            current = open.poll();
            if (current == null){
                return null;
            }
            if (current.getLastSight().equals(tc.getEndSight()) && current.getPath().length >= 2){
                if (current.containsIncludedSights(tc)){
                    return new Path(current.getPath());
                }
            }else{
                this.handleNextNodes(tc, allSights, current, closed, open);
            }
        }
    }

    private void handleNextNodes(TripConfigurations tc, Set<Sight> allSights,
                                 PathNode node, Map<PathNode, PathNode> closed, PathfinderPathNodePriorityQueue open) throws ExecutionException, InterruptedException {
        closed.put(node, node);
        for (PathNode neighbour : node.getNeighbours(allSights, tc)){
            if (!closed.containsKey(neighbour) && !open.containsPathNodeWith(neighbour)){
                open.offer(neighbour);
            }
        }
    }
}
