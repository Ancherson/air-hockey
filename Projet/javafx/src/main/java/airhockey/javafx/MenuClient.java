package airhockey.javafx;

import airhockey.model.Model;
import airhockey.network.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.SocketException;

public class MenuClient extends Application {
    private int WIDTH = 800;
    private int HEIGHT = 500;
    private Stage primaryStage;

    private Scene scene1;
    private Scene scene2;
    private Scene scene3;
    private Scene scene4;

    private Window window;
    private Model model = new Model();

    private Client client;

    private FirstMenu pane;
    private JoinMenu joinMenu;
    private View view;
    private CreateMenu create;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        window = (Window) primaryStage;

        pane = new FirstMenu(this);
        joinMenu = new JoinMenu(this);
        view = new View(this, model,1);
        create = new CreateMenu(this);

        scene1 = new Scene(pane);
        scene2 = new Scene(create);
        scene3 = new Scene(joinMenu);
        scene4 = new Scene(view);

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
        switch (S){
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
                new Thread (() ->{
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
                window.setHeight(540);
                window.setWidth(820);
                primaryStage.setScene(scene4);

        }
    }

    public void setView(int numplayer){
        View view = new View(this, model,numplayer);
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

    public void closeClient() {
        client.close();
    }

}
