package com.example.simon.vilshofenguide.pathfinding;

import android.os.AsyncTask;

import com.example.simon.vilshofenguide.R;
import com.example.simon.vilshofenguide.view.StringRessourceManager;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 23.03.2016.
 */
public class GoogleMapsPathfinder{

    private LatLng origin;
    private LatLng destination;
    private JSONObject downloadedJSON;

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String str_key = "key=" + StringRessourceManager.getInstance().getString(R.string.google_maps_key);

        String parameters = str_origin + "&" + str_dest + "&mode=walking";
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&" + str_key;

        return url;
    }

    private void download() {
        try {
            //Use get() as a method to join the task
            this.downloadedJSON = new DownloadTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> getPath(JSONObject downloadedJSON){

        List<LatLng> path = new ArrayList<LatLng>() ;
        JSONArray steps = null;

        try {
            steps = ((JSONObject)  ((JSONObject) downloadedJSON.getJSONArray("routes").get(0)  ).getJSONArray("legs").get(0))
                    .getJSONArray("steps");

            for(int i = 0; i < steps.length(); i++){
                String polyline = "";
                polyline = (String)((JSONObject)((JSONObject)steps.get(i)).get("polyline")).get("points");
                List<LatLng> list = decodePoly(polyline);
                /** Traversing all points */
                for(int k = 0; k < list.size(); k++){
                    path.add((LatLng)list.get(k));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return path;
    }

    private int getDurationInSeconds(JSONObject downloadedJSON){
        JSONObject route = null;

        try {
            route = (JSONObject)downloadedJSON.getJSONArray("routes").get(0);
            return ((JSONObject)route.getJSONArray("legs").get(0)) .getJSONObject("duration").getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void setOrigin(LatLng origin){
        this.origin = origin;
        this.downloadedJSON = null;
    }

    public void setDestination(LatLng destination){
        this.destination = destination;
        this.downloadedJSON = null;
    }

    public List<LatLng> calculatePath(){
        if (this.downloadedJSON == null){
            this.download();
        }
        return getPath(downloadedJSON);
    }

    public int calculateDurationInSeconds(){
        if (this.downloadedJSON == null){
            this.download();
        }
        return getDurationInSeconds(downloadedJSON);
    }

    /**
     * Decodes the polyline
     * Algorithm from : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private class DownloadTask extends AsyncTask<Void, Void, JSONObject>{

        protected JSONObject doInBackground(Void... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(getDirectionsUrl(origin, destination));

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();

                String line = "";
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }

                br.close();


                return new JSONObject(result.toString());
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
