package com.example.simon.vilshofenguide.pathfinding;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Simon on 04.03.2017.
 */

public class DistanceInitializer {

    public DistanceInitializingTask initDistances(SightManager m) throws MalformedURLException {
        DistanceInitializingTask task = new DistanceInitializingTask();
        return (DistanceInitializingTask)task.execute(new URL(SightInitializer.serverAdress + "distances.txt"), m);
    }

}
