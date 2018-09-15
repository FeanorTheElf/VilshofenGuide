package com.example.simon.vilshofenguide.sightseeing;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
