package airhockey.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;


import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

public class FirstMenu extends BorderPane {

    private Sound sound;

    public FirstMenu(MenuClient Menu){
        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        sound = new Sound();

        Label title = new Label("AIR-HOCKEY");
        title.setStyle("-fx-font : 32 Ubuntu;");
        title.setTextFill(WHITE);

        Button create = new Button("Create room");
        Button join = new Button("Join a room");
        Button joinPublic = new Button("Join Public Room");
        Button training = new Button("TRAINING MODE");

        Image unmute = new Image(new File("../ressources/UnMuteButton.png").toURI().toString());
        Image mute = new Image(new File("../ressources/MuteButton.png").toURI().toString());     
        Button stopSound = new Button();


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
            sound.play("buttonsRelax");
            Menu.setScene("creation");

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
            sound.play("buttonsRelax");
            Menu.setScene("join");
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
            sound.play("buttonsRelax");
            Menu.setScene("wait");
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
            sound.play("buttonsRelax");
            Menu.getModel().setBot(0);
            Menu.setView(1);
        });

        stopSound.setPrefSize(50,55);
        stopSound.setMinWidth(50);
        stopSound.setStyle("-fx-background-color: #565656;");
        stopSound.setTextFill(WHITE);
        stopSound.setGraphic(new ImageView(unmute));

        stopSound.setOnMouseEntered((action)->{
            stopSound.setStyle("-fx-background-color: #FFFFFF;");
            stopSound.setTextFill(BLACK);
        });

        stopSound.setOnMouseExited((action)->{
            stopSound.setStyle("-fx-background-color: #565656;");
            stopSound.setTextFill(WHITE);
        });
        
        stopSound.setOnAction(value ->{
            sound.play("buttonsRelax");
            if(Menu.getIsPaused()){
                Menu.setIsPaused(!Menu.getIsPaused());
                stopSound.setGraphic(new ImageView(unmute));
                Menu.repeatSound();
            }else{
                Menu.setIsPaused(!Menu.getIsPaused());
                stopSound.setGraphic(new ImageView(mute));
                Menu.pauseSound();
            }
        });

        this.setAlignment(boxV2, Pos.CENTER);
        this.setCenter(boxV2);

        this.setAlignment(training,Pos.BASELINE_RIGHT);
        this.setBottom(training);

        this.setAlignment(stopSound, Pos.TOP_LEFT);
        this.setTop(stopSound);

        this.setPadding(new Insets(30,50,50,50));



    }

}
