package airhockey.gui;

import airhockey.model.Model;
import airhockey.network.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.media.*;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.File;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This Class is the main class of the GUI.
 * It contains all the different pages and multiple function of the GUI
 */
public class MenuClient extends Application {
    private int WIDTH = 800;
    private int HEIGHT = 500;
    private Stage primaryStage;
    private Sound sound;
    private boolean isPaused;

    private Scene first;
    private Scene creation;
    private Scene join;
    private Scene wait;
    private Scene game;

    private Window window;
    private Model model = new Model();
    private View view;

    private Client client;

    private FirstMenu pane;
    private JoinMenu joinMenu;
    private CreateMenu create;
    private PublicWait waiting;

    /**
     * Function equivalent to the constructor. It launches the Javafx Thread
     * @param primaryStage The stage that will contain every elements of the GUI
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        window = (Window) primaryStage;


        primaryStage.setOnHiding(this::close);
        pane = new FirstMenu(this);
        first = new Scene(pane);

        create = new CreateMenu(this);
        waiting = new PublicWait(this);


        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);

        window.setHeight(HEIGHT);
        window.setWidth(WIDTH);

        sound = new Sound();
        sound.repeat("ambianceRelax");
        isPaused = false;


        primaryStage.setScene(first);
        primaryStage.show();


        primaryStage.widthProperty().addListener((obs,oldVal,newVal) ->{
            if(view != null) {
                view.resizeCanvas(newVal.doubleValue() - 20, -1);
            }
        });

        primaryStage.heightProperty().addListener((obs,oldVal,newVal) ->{
            if(view != null) {
                view.resizeCanvas(-1, newVal.doubleValue() - 100);
            }
        });
    }

    /**
     * Function that swaps to the different pages of the GUI
     * @param S is the String that refers to the name of the page, for example "first" for the first page
     */
    public void setScene(String S) {
        switch (S) {
            case "first":
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(first);
                primaryStage.setMinHeight(300);
                primaryStage.setMinWidth(400);
                break;

            case "creation":
                create = new CreateMenu(this);
                creation = new Scene(create);
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(creation);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);

                System.out.println("EN ATTENTE DU SERVEUR");
                new Thread(() -> {
                    createRoom();
                }).start();


                System.out.println("Ca commence !!!");
                break;

            case "join":
                joinMenu = new JoinMenu(this);
                join = new Scene(joinMenu);
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(join);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                break;

            case "wait":
                waiting = new PublicWait(this);
                wait = new Scene(waiting);
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(wait);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);

                System.out.println("EN ATTENTE DU SERVEUR");
                new Thread(() -> {
                    joinPublicRoom();
                }).start();
                break;
            case "game":
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                primaryStage.setScene(game);
                break;
        }
    }

    /**
     * Function that returns the model of the game used by the GUI
     * @return a model
     */
    public Model getModel(){
        return model;
    }

    /**
     * Function to mute the background sound
     */
    public void pauseSound(){
        sound.pause("ambianceRelax");
    }

    /**
     * Function to loop the background sound
     */
    public void repeatSound(){
        sound.reload("ambianceRelax");
        sound.repeat("ambianceRelax");
    }

    /**
     * Function that sets the isPaused boolean that refers to is the background sound is played or not
     * @param b is the boolean value you give to isPaused
     */
    public void setIsPaused(boolean b){
        isPaused = b;
    }

    /**
     * Function that gives the isPaused boolean that refers to is the background sound is played or not
     * @return the boolean value of isPaused
     */
    public boolean getIsPaused(){
        return isPaused;
    }

    /**
     * Function that will swap the vision of the game and lanch the game
     * @param numplayer
     * @param training
     */
    public void setView(int numplayer, boolean training) {
        view = new View(this, model, numplayer, training);
        game = new Scene(view);
        view.resizeCanvas(primaryStage.getWidth() - 20, -1);
        view.resizeCanvas(-1,primaryStage.getHeight() - 100);
        setScene("game");
    }

    /**
     * Function that will ask the creation of a room to the server by creating a Client and that swap to the creation page
     */
    public void createRoom() {
        try {
            client = new Client(model,Platform::runLater,create::setID,waiting::connected,this::lostConnexion);
            client.createRoom();
            Platform.runLater(() -> setView(0, false));
        } catch (SocketException e) {
            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that will ask to join the room that correspond to the id to the server by creating a Client
     * and that will swap to the join page
     * @param id is the String that le player typed referring to the id of the room that he wants to join
     */
    public void joinRoom(String id) {
        try {
            client = new Client(model, Platform::runLater, create::setID,waiting::connected,this::lostConnexion, joinMenu::setMessage);
            if(client.joinRoom(id)) {
                setView(1, false);
            }
        } catch (SocketException e) {
            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that create a Client then launch the Function joinRoomPublic that create a public room or join one
     */
    public void joinPublicRoom() {
        try {
            client = new Client(model,Platform::runLater,create::setID,waiting::connected,this::lostConnexion);
            client.joinRoomPublic();
            Platform.runLater(() -> {
                System.out.println(client.getNumPlayer());
                setView(client.getNumPlayer(), false);
            });
        } catch (SocketException e) {
            System.out.println("Socket closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that return the boolean isFinished of Client
     * @return the boolean isFinished of Client
     */
    public boolean isFinished() {
        return client.isFinished();
    }

    /**
     * Function that launch the function lostConnexion of View
     */
    public void lostConnexion() {
        view.lostConnexion();
    }

    /**
     * Function that close the client
     */
    public void closeClient()  {
        if(client!= null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Function that close the running game
     */
    public void close() {
        model = new Model();
        close(null);
    }

    /**
     * Function that close the Thread animation of View
     * @param windowEvent Event that should be the close of the window or null if called by close()
     */
    public void close(WindowEvent windowEvent) {
        if(view != null) {
            view.close();
            view = null;
        }
        closeClient();
    }
}
