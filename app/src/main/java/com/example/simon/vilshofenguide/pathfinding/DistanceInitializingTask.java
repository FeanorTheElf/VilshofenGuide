package com.example.simon.vilshofenguide.pathfinding;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Simon on 04.03.2017.
 */

public class DistanceInitializingTask extends AsyncTask<Object, Void, Void> implements Serializable {

    protected Void doInBackground(Object... params) {
        URL url = (URL)params[0];
        SightManager m = (SightManager)params[1];
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine(); // we only have one line in this file
            reader.close();

            Pattern p = Pattern.compile("(\\d*),(\\d*)=([\\dE\\.]*)");
            for (String distance : line.split("#")){
                Matcher mr = p.matcher(distance);
                mr.matches();
                int firstIndex = Integer.parseInt(mr.group(1));
                int secondIndex = Integer.parseInt(mr.group(2));
                double dist = Double.parseDouble(mr.group(3));
                m.setDistance(firstIndex, secondIndex, dist);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.disconnect();
        }
        return null;
    }
}