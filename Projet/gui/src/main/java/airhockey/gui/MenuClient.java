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

    private Scene scene1;
    private Scene scene2;
    private Scene scene3;
    private Scene scene4;
    private Scene scene5;

    private Window window;
    private Model model = new Model();

    private Client client;

    private FirstMenu pane;
    private JoinMenu joinMenu;
    private CreateMenu create;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        window = (Window) primaryStage;


        primaryStage.setOnHiding(this::close);
         pane = new FirstMenu(this);
         joinMenu = new JoinMenu(this);
         create = new CreateMenu(this);

        scene1 = new Scene(pane);
        scene2 = new Scene(create);
        scene3 = new Scene(joinMenu);

        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);

        window.setHeight(HEIGHT);
        window.setWidth(WIDTH);

        primaryStage.setScene(scene1);
        primaryStage.show();



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

    public void setScene(int S) {
        switch (S) {
            case 1:
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(scene1);
                primaryStage.setMinHeight(300);
                primaryStage.setMinWidth(400);
                break;

            case 2:
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(scene2);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);

                System.out.println("EN ATTENTE DU SERVEUR");
                new Thread(() -> {
                    createRoom();
                }).start();


                System.out.println("Ca commence !!!");
                break;

            case 3:
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(scene3);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                break;
            case 4:
/*                window.setHeight(600);
                window.setWidth(820);*/
                primaryStage.setMinHeight(600);
                primaryStage.setMinWidth(820);
                primaryStage.setScene(scene4);
                break;
            case 5:
                window.setHeight(primaryStage.getHeight());
                window.setWidth(primaryStage.getWidth());
                primaryStage.setScene(scene5);
                primaryStage.setMinHeight(330);
                primaryStage.setMinWidth(400);
                break;
        }
    }

    public void setView(int numplayer) {
        //model = new Model();
        View view = new View(this, model, numplayer);
        scene4 = new Scene(view);
        setScene(4);
    }

    public void createRoom() {
        try {
            client = new Client(model,Platform::runLater,create::setID);
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
            client = new Client(model, Platform::runLater, create::setID);
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
            client = new Client(model,Platform::runLater,create::setID);
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
        scene5 = new Scene(end);
        setScene(5);
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
        if(scene4 != null) ((View)(scene4.getRoot())).close();
        if(client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
