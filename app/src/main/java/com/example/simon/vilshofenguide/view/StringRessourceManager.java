package com.example.simon.vilshofenguide.view;

/**
 * Created by Simon on 20.01.2016.
 */
public class StringRessourceManager {

    private static StringRessourceManager singleton;

    public static StringRessourceManager getInstance(){
        if (singleton == null){
            singleton = new StringRessourceManager();
        }
        return singleton;
    }

    public String getString(int id){

        return StaticContextActivity.getContext().getString(id);
    }

    private StringRessourceManager(){
    }
}
