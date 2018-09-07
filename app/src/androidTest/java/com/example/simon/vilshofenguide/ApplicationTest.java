package com.example.simon.vilshofenguide;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.example.simon.vilshofenguide.newPathfinding.PathfinderNew;
import com.example.simon.vilshofenguide.pathfinding.Category;
import com.example.simon.vilshofenguide.pathfinding.Path;
import com.example.simon.vilshofenguide.pathfinding.SightManager;
import com.example.simon.vilshofenguide.pathfinding.TripConfigurations;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

}