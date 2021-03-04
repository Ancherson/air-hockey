package Projet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MenuClient extends Application {
    private int WIDTH = 800;
    private int HEIGHT = 500;


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setMinHeight(60);
        primaryStage.setMinWidth(200);


        Button create = new Button("Create room");
        Button join = new Button("join a room");
        Button backJ = new Button("back");
        Button backC = new Button("back");



        Label creation = new Label("creation de la room");
        Label joinT = new Label("rentrer l'id");

        TextField id = new TextField();
        HBox BoxB1 = new HBox(create,join);
        //BoxB1.setPadding(new Insets(15,12,15,12));
        BoxB1.setSpacing(0);

        StackPane pane = new StackPane(BoxB1);
        pane.setAlignment(BoxB1, Pos.CENTER);
        Pane createMenu = new Pane(creation,backJ);
        Pane joinMenu = new Pane(joinT,id,backC);

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
        });

        Scene scene1 = new Scene(pane);
        Scene scene2 = new Scene(createMenu);
        Scene scene3 = new Scene(joinMenu);


        primaryStage.setHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);

        create.setPrefSize(100,60);
        create.setMinWidth(100);
        create.setOnAction(event -> {
            primaryStage.setScene(scene2);
        });


        join.setPrefSize(100,60);
        join.setMinWidth(100);
        join.setOnAction(value -> {
            primaryStage.setScene(scene3);
        });

        creation.setPrefHeight(100);
        creation.setLayoutX(300);
        creation.setLayoutY(200);

        backJ.setMaxSize(100,60);
        backJ.setPrefSize(100,60);
        backJ.setLayoutX(700);
        backJ.setLayoutY(440);
        backJ.setOnAction(value -> {
            primaryStage.setScene(scene1);
        });

        backC.setMaxSize(100,60);
        backC.setLayoutX(700);
        backC.setLayoutY(440);
        backC.setOnAction(value -> {
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
}
