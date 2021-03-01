package Projet;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static com.sun.javafx.css.StyleClassSet.getStyleClass;


public class MenuClient extends Application {
    private final int WIDTH = 800;
    private final int HEIGHT = 500;


    /*private Button B = new Button("Create room");
    private Label L = new Label("coucou changement de scene");
    private Pane pane = new Pane();
    */

    @Override
    public void start(Stage primaryStage) throws Exception {

        Button create = new Button("Create room");
        Button join = new Button("join a room");
        Button back = new Button("back");

        Label creation = new Label("creation de la room");
        Label joinT = new Label("rentrer l'id");

        TextField id = new TextField();

        Pane pane = new Pane(create,join);
        Pane createMenu = new Pane(creation,back);
        Pane joinMenu = new Pane(joinT,id,back);


        Scene scene1 = new Scene(pane);
        Scene scene2 = new Scene(createMenu);
        Scene scene3 = new Scene(joinMenu);

        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);

        create.setMaxSize(100,60);
        create.setLayoutX(200);
        create.setLayoutY(200);
        create.setOnAction(value -> {
            primaryStage.setScene(scene2);
        });

        join.setMaxSize(100,60);
        join.setLayoutX(300);
        join.setLayoutY(200);
        join.setOnAction(value -> {
            primaryStage.setScene(scene3);
        });

        creation.setMinHeight(100);
        creation.setMaxHeight(100);
        creation.setLayoutX(300);
        creation.setLayoutY(200);

        back.setMaxSize(100,60);
        back.setLayoutX(300);
        back.setLayoutY(200);
        back.setOnAction(value -> {
            primaryStage.setScene(scene1);
        });

        joinT.setMinHeight(100);
        joinT.setMaxHeight(100);
        joinT.setLayoutX(300);
        joinT.setLayoutY(200);

        id.setLayoutY(300);
        id.setLayoutX(300);
        id.setPromptText("id");
        id.setOnMousePressed( event -> {
            id.selectAll();
        });
        id.setOnAction(event -> {
            System.out.println(id.getText());
        });


        primaryStage.setScene(scene1);
        primaryStage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
}
