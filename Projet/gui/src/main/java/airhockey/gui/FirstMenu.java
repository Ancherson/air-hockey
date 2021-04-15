package airhockey.gui;

import javafx.application.Platform;
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

        ClickButton create = new ClickButton("Create room");
        ClickButton join = new ClickButton("Join a room");
        ClickButton joinPublic = new ClickButton("Join Public Room");
        ClickButton training = new ClickButton("TRAINING MODE");

        HBox boxB1 = new HBox(25.0,create,join);    
        VBox boxV1 = new VBox(25.0,boxB1,joinPublic);
        VBox boxV2 = new VBox(50.0,title,boxV1); 
        boxB1.setAlignment(Pos.CENTER);
        boxV1.setAlignment(Pos.CENTER);
        boxV2.setAlignment(Pos.CENTER);


        create.setPrefSize(130,55);
        create.setMinWidth(130);

        create.setOnAction(event -> {
            Menu.setScene("creation");

        });

        join.setPrefSize(130,55);
        join.setMinWidth(130);

        join.setOnAction(value -> {
            Menu.setScene("join");
        });

        joinPublic.setPrefSize(130,55);
        joinPublic.setMinWidth(130);


        joinPublic.setOnAction(value ->{
            Menu.setScene("wait");
        });

        training.setPrefSize(130,55);
        training.setMinWidth(130);
        
        training.setOnAction(value ->{
            Menu.getModel().setBot(0);
            Menu.setView(1, true);
        });

        this.setAlignment(boxV2, Pos.CENTER);
        this.setCenter(boxV2);

        this.setAlignment(training,Pos.BASELINE_RIGHT);
        this.setBottom(training);

        this.setPadding(new Insets(30,50,50,50));

    }

}
