package airhockey.gui;

import java.io.File;
import java.util.*;

import javafx.scene.media.*;
import javafx.util.Duration;

/**
 * This class is used to play sound in the GUI
 */
public class Sound {

    /**
     * Saves all media with a given name
     */
    private HashMap<String,MediaPlayer> playlist;

    /**
     * Path of the media
     */
    private String path;

    /**
     * Open the media
     */
    private Media media;

    /**
     * Open the media in the mediaplayer
     */
    private MediaPlayer mediaPlayer;

    /**
     * Constructor of sounds
     * It saves all media in a playlist
     */
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

        path = "../ressources/relax_buttons.wav";
        media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        playlist.put("buttonsRelax",mediaPlayer);
    }

    /**
     * Plays the media of the name given in param
     * @param name of the media in the playlist
     */
    public void play(String name){
        playlist.get(name).seek(Duration.ZERO);
        playlist.get(name).play();
    }

    /**
     * Plays in loop the media of the name given in param
     * @param name of the media in the playlist
     */
    public void repeat(String name){
        playlist.get(name).setOnEndOfMedia(() -> playlist.get(name).seek(Duration.ZERO));
        playlist.get(name).play();

    }

    /**
     * Stops the media of the name given in param
     * @param name of the media in the playlist
     */
    public void pause(String name){
        playlist.get(name).pause();
    }

    /**
     * Reloads the media of the name given in param
     * @param name of the media in the playlist
     */
    public void reload(String name){
        playlist.get(name).seek((playlist.get(name).getStartTime()));
    }

    /**
     * Modifies the volume of the media of the name given in param
     * @param name of the media in the playlist
     * @param degree double of the new volume
     */
    public void setVolume(String name, double degree){
        playlist.get(name).setVolume(degree);
    }
    
}
