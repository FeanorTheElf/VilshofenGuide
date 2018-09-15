package com.example.simon.vilshofenguide.sightseeing;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 03.01.2016.
 */
public interface SightManager extends Serializable{

    List<NamedRoute> getPredefinedRoutes();

    Sight getSightById(int id);

    Sight getSightByName(String stringValueOfSight);

    double getDistanceInMinutes(Sight sight1, Sight sight2);

    Sight getAnySight();

    Collection<Sight> getAllSights();
}
