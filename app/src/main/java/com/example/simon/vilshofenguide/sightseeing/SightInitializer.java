package com.example.simon.vilshofenguide.sightseeing;

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
 * Created by Simon on 05.10.2016.
 */
public class SightInitializer {

    public static final String serverURL = "www.vilshofen.info";
    public static final String serverAdress = "http://" + serverURL + "/";

    public AsyncTask<?, ?, SightManager> downloadSightManager() throws MalformedURLException {
        DownloadTask d = new DownloadTask();
        d.execute(new URL(serverAdress + "sight_data.txt"));
        return d;
    }

    private static class DownloadTask extends AsyncTask<URL, Void, SightManager>{

        protected SightManager doInBackground(URL... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            try {
                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) params[0].openConnection();
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser s = factory.newSAXParser();
                XMLSightLoader handler = new XMLSightLoader();
                s.parse(inputStream, handler);
                //Initializing distances
                DistanceInitializingTask task = new DistanceInitializingTask();
                //As we cannot start a new thread from the not-main-thread, just execute this sequentially.
                //we already work concurrently, remember?
                task.doInBackground(new URL(SightInitializer.serverAdress + "distances.txt"), handler.getSightManager());
                return handler.getSightManager();
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


    private static class DistanceInitializingTask extends AsyncTask<Object, Void, Void> implements Serializable {

        protected Void doInBackground(Object... params) {
            URL url = (URL) params[0];
            ConfigurableSightManager m = (ConfigurableSightManager) params[1];
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
                for (String distance : line.split("#")) {
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
}
