package airhockey.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class FirstMenu extends BorderPane {

    public FirstMenu(MenuClient Menu){
        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        Label title = new Label("AIR-HOCKEY");
        title.setStyle("-fx-font : 32 Ubuntu;");
        title.setTextFill(WHITE);

        Button create = new Button("Create room");
        Button join = new Button("Join a room");
        Button joinPublic = new Button("Join Public Room");
        Button training = new Button("TRAINING MODE");

        HBox boxB1 = new HBox(25.0,create,join);    
        VBox boxV1 = new VBox(25.0,boxB1,joinPublic);
        VBox boxV2 = new VBox(50.0,title,boxV1); 
        boxB1.setAlignment(Pos.CENTER);
        boxV1.setAlignment(Pos.CENTER);
        boxV2.setAlignment(Pos.CENTER);


        create.setPrefSize(130,55);
        create.setMinWidth(130);
        create.setStyle("-fx-background-color: #565656;");
        create.setTextFill(WHITE);

        create.setOnMouseEntered((action)->{
            create.setStyle("-fx-background-color: #FFFFFF;");
            create.setTextFill(BLACK);
        });

        create.setOnMouseExited((action)->{
            create.setStyle("-fx-background-color: #565656;");
            create.setTextFill(WHITE);
        });
        create.setOnAction(event -> {
            Menu.setScene(2);

        });

        join.setPrefSize(130,55);
        join.setMinWidth(130);
        join.setStyle("-fx-background-color: #565656;");
        join.setTextFill(WHITE);

        join.setOnMouseEntered((action)->{
            join.setStyle("-fx-background-color: #FFFFFF;");
            join.setTextFill(BLACK);
        });

        join.setOnMouseExited((action)->{
            join.setStyle("-fx-background-color: #565656;");
            join.setTextFill(WHITE);
        });

        join.setOnAction(value -> {
            Menu.setScene(3);
        });

        joinPublic.setPrefSize(130,55);
        joinPublic.setMinWidth(130);
        joinPublic.setStyle("-fx-background-color: #565656;");
        joinPublic.setTextFill(WHITE);

        joinPublic.setOnMouseEntered((action)->{
            joinPublic.setStyle("-fx-background-color: #FFFFFF;");
            joinPublic.setTextFill(BLACK);
        });

        joinPublic.setOnMouseExited((action)->{
            joinPublic.setStyle("-fx-background-color: #565656;");
            joinPublic.setTextFill(WHITE);
        });

        joinPublic.setOnAction(value ->{
            Menu.joinPublicRoom();
        });

        training.setPrefSize(130,55);
        training.setMinWidth(130);
        training.setStyle("-fx-background-color: #565656;");
        training.setTextFill(WHITE);

        training.setOnMouseEntered((action)->{
            training.setStyle("-fx-background-color: #FFFFFF;");
            training.setTextFill(BLACK);
        });

        training.setOnMouseExited((action)->{
            training.setStyle("-fx-background-color: #565656;");
            training.setTextFill(WHITE);
        });
        
        training.setOnAction(value ->{
            Menu.setView(1);
        });

        this.setAlignment(boxV2, Pos.CENTER);
        this.setCenter(boxV2);

        this.setAlignment(training,Pos.BASELINE_RIGHT);
        this.setBottom(training);

        this.setPadding(new Insets(30,50,50,50));



    }

}
