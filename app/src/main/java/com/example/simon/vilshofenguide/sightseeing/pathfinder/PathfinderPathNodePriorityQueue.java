package com.example.simon.vilshofenguide.sightseeing.pathfinder;

import com.example.simon.vilshofenguide.sightseeing.TripConfigurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 29.11.2016.
 */
class PathfinderPathNodePriorityQueue {

    private TripConfigurations tc;
    //greatest element at the end of the list
    private List<PathNode> queue;
    private Map<PathNode, PathNode> map;

    public PathNode getPathNodeWith(PathNode content){
        return map.get(content);
    }

    public int size(){
        return queue.size();
    }

    public boolean containsPathNodeWith(PathNode content){
        return map.containsKey(content);
    }

    private int compare(PathNode o1, PathNode o2){
        return Double.compare(o1.evaluate(tc), o2.evaluate(tc));
    }

    public int indexOf(PathNode node){
        for (int i = (getIndex(node) + 1); i < this.queue.size() && compare(this.queue.get(i), node) == 0; i++){
            if (this.queue.get(i).equals(node)){
                return i;
            }
        }
        return -1;
    }

    public boolean contains(PathNode node){
        return indexOf(node) != -1;
    }

    public PathNode poll(){
        if (this.queue.isEmpty()){
            return null;
        }
        PathNode result = this.queue.remove(this.queue.size() - 1);
        this.map.remove(result);
        return result;
    }

    public void offer(PathNode node){
        int i = getIndex(node);
        this.queue.add(i + 1, node);
        this.map.put(node, node);
    }

    public void remove(int index){
        this.map.remove(this.queue.remove(index));
    }

    public void set(int i, PathNode node){
        this.remove(i);
        this.offer(node);
    }

    public PathNode get(int i){
        return this.queue.get(i);
    }

    /**
     * Returns the index the element at is the greatest element smaller than node
     * @param node
     * @return
     */
    private int getIndex(PathNode node){
        if (this.queue.size() == 0){
            return -1;
        }
        int bottomIndex = 0;
        int topIndex = queue.size() - 1;
        while ((topIndex - bottomIndex) > 1){
            int newIndex = (int)((topIndex + bottomIndex) / 2.);
            if (compare(node, this.queue.get(newIndex)) <= 0){
                topIndex = newIndex;
            }else{
                bottomIndex = newIndex;
            }
        }
        if (compare(node, this.queue.get(topIndex)) > 0){
            return topIndex;
        }else if (compare(node, this.queue.get(bottomIndex)) <= 0){
            return bottomIndex - 1;
        }else{
            return bottomIndex;

        }
    }

    public PathfinderPathNodePriorityQueue(TripConfigurations tc){
        this.queue = new ArrayList<>();
        this.map = new HashMap<>();
        this.tc = tc;
    }
}
