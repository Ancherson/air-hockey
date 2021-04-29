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
    private Scene last;

    private Window window;
    private Model model = new Model();
    private View view;

    private Client client;

    private FirstMenu pane;
    private JoinMenu joinMenu;
    private CreateMenu create;
    private PublicWait waiting;

    @Override
    public void start(Stage primaryStage) throws Exception {
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
            case "last":
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(last);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                break;
        }
    }

    public Model getModel(){
        return model;
    }

    public void pauseSound(){
        sound.pause("ambianceRelax");
    }

    public void repeatSound(){
        sound.reload("ambianceRelax");
        sound.repeat("ambianceRelax");
    }

    public void setIsPaused(boolean b){
        isPaused = b;
    }

    public boolean getIsPaused(){
        return isPaused;
    }


    public void setView(int numplayer, boolean training) {
        view = new View(this, model, numplayer, training);
        game = new Scene(view);
        view.resizeCanvas(primaryStage.getWidth() - 20, -1);
        view.resizeCanvas(-1,primaryStage.getHeight() - 100);
        setScene("game");
    }

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

    public void joinPublicRoom() {
        try {
            client = new Client(model,Platform::runLater,create::setID,waiting::connected,this::lostConnexion, joinMenu::setMessage);
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

    public void endRoom(boolean won){
        close();
        EndMenu end = new EndMenu(this, won);
        last = new Scene(end);
        setScene("last");
    }

    public void lostConnexion() {
        view.lostConnexion();
    }

    public void closeClient()  {
        if(client!= null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        model = new Model();
        close(null);
    }

    public void close(WindowEvent windowEvent) {
        if(view != null) {
            view.close();
            view = null;
        }
        closeClient();
    }
}
