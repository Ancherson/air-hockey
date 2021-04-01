package airhockey.gui;

import airhockey.model.Model;
import airhockey.network.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.SocketException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MenuClient extends Application {
    private int WIDTH = 800;
    private int HEIGHT = 500;
    private Stage primaryStage;

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
         joinMenu = new JoinMenu(this);
         create = new CreateMenu(this);
         waiting = new PublicWait(this);

        first = new Scene(pane);
        creation = new Scene(create);
        join = new Scene(joinMenu);
        wait = new Scene(waiting);

        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);

        window.setHeight(HEIGHT);
        window.setWidth(WIDTH);

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
        /*
            Fonction qui permet de center un Stackpane nommer "pane"

            primaryStage.widthProperty().addListener((obs,oldVal,newVal) ->{
            WIDTH = newVal.intValue();
            int heigth = HEIGHT/ 2 -60;
            int width = newVal.intValue()/2-100;
            if((width) <0) width =0;
            Insets pad = new Insets(heigth,width,heigth,width);
            pane.setPadding(pad);
        });

        primaryStage.heightProperty().addListener((obs,oldVal,newVal) ->{
            HEIGHT = newVal.intValue();
            int width = WIDTH/ 2 -100;
            int heigth = newVal.intValue()/2-60;
            if((heigth) <0) heigth =0;
            Insets pad = new Insets(heigth,width,heigth,width);
            pane.setPadding(pad);
        });*/

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
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(join);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                break;

            case "wait":
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

                primaryStage.setMinHeight(600);
                primaryStage.setMinWidth(820);
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

    public void setView(int numplayer) {
        //model = new Model();
        view = new View(this, model, numplayer);
        game = new Scene(view);
        System.out.println("AVANT");
        view.resizeCanvas(primaryStage.getWidth() - 20, -1);
        view.resizeCanvas(-1,primaryStage.getHeight() - 100);
        System.out.println("APRES");
        setScene("game");
    }

    public void createRoom() {
        try {
            client = new Client(model,Platform::runLater,create::setID,waiting::connected);
            client.createRoom();
            Platform.runLater(() -> setView(0));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(String id) {
        try {
            client = new Client(model, Platform::runLater, create::setID,waiting::connected);
            client.joinRoom(id);
            setView(1);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinPublicRoom() {
        try {
            client = new Client(model,Platform::runLater,create::setID,waiting::connected);
            client.joinRoomPublic();
            Platform.runLater(() -> {
                System.out.println(client.getNumPlayer());
                setView(client.getNumPlayer());
            });
        } catch (SocketException e) {
            e.printStackTrace();
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

    public void closeClient()  {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        if(client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
