package airhockey.gui;

import java.io.File;
import java.util.*;

import javafx.scene.media.*;
import javafx.util.Duration;


public class Sound {
    private HashMap<String,MediaPlayer> playlist;
    private String path;
    private Media media;
    private MediaPlayer mediaPlayer;

    public Sound(){
        playlist = new HashMap<>();

        path = "../ressources/bruit-goutte-deau.wav";
        media = new Media(new File(path).toURI().toString());       
        mediaPlayer = new MediaPlayer(media);
        playlist.put("collisionRelax", mediaPlayer);

        path = "../ressources/thunder.wav";
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        playlist.put("shakingRelax",mediaPlayer);

        path = "../ressources/ambiance_relax.wav";
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        playlist.put("ambianceRelax",mediaPlayer);
    }

    public void play(String name){
        playlist.get(name).seek(playlist.get(name).getStartTime());
        playlist.get(name).play();
    }

    public void repeat(String name){
        playlist.get(name).setOnEndOfMedia(new Runnable(){
            public void run(){
                playlist.get(name).seek(Duration.ZERO);
            }
        });
        playlist.get(name).play();

    }

    public void stop(String name){
        playlist.get(name).stop();
    }
    
}
