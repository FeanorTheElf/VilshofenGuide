package com.example.simon.vilshofenguide.pathfinding;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Simon on 05.10.2016.
 */
public class SightInitializer {

    public static final String serverURL = "www.vilshofen.info";
    public static final String serverAdress = "http://" + serverURL + "/";

    public AsyncTask<?, ?, SightManager> downloadSightManager() throws MalformedURLException, ExecutionException, InterruptedException {
        DownloadTask d = new DownloadTask();
        d.execute(new URL(serverAdress + "sight_data.txt"));
        return d;
    }

    private class DownloadTask extends AsyncTask<URL, Void, SightManager>{

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
}
