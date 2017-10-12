package com.soto.videoprecios;

import java.util.List;

/**
 * Created by José on 08/10/2017.
 */

public class VideogameListManager {

    private static final VideogameListManager manager = new VideogameListManager();

    private VideogameListManager(){
        ;
    }

    public static VideogameListManager callManager(){
        return manager;
    }

    private List<Videogame> videogameList;

    public List<Videogame> getVideogameList() {
        return videogameList;
    }

    public void setVideogameList(List<Videogame> videogameList) {
        this.videogameList = videogameList;
    }
}
