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
/**
 * This class represents the main page of the GUI, sort of the Main Menu
 */

public class FirstMenu extends BorderPane {

    private Sound sound;
    /**
     * Constructor of the first page
     * @param Menu is the main Pane of the gui that allows to navigate to different page
     */
    public FirstMenu(MenuClient Menu){
        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        sound = new Sound();

        Label title = new Label("AIR-HOCKEY");
        title.setStyle("-fx-font : 32 Ubuntu;");
        title.setTextFill(WHITE);

        ClickButton create = new ClickButton("Create room");
        ClickButton join = new ClickButton("Join a room");
        ClickButton joinPublic = new ClickButton("Join Public Room");
        ClickButton training = new ClickButton("TRAINING MODE");

        Image unmute = new Image(new File("../ressources/UnMuteButton.png").toURI().toString());
        Image mute = new Image(new File("../ressources/MuteButton.png").toURI().toString());     
        ClickButton stopSound = new ClickButton();


        HBox boxB1 = new HBox(25.0,create,join);    
        VBox boxV1 = new VBox(25.0,boxB1,joinPublic);
        VBox boxV2 = new VBox(50.0,title,boxV1); 
        boxB1.setAlignment(Pos.CENTER);
        boxV1.setAlignment(Pos.CENTER);
        boxV2.setAlignment(Pos.CENTER);


        create.setPrefSize(130,55);
        create.setMinWidth(130);

        create.setOnAction(event -> {
            sound.play("buttonsRelax");
            Menu.setScene("creation");

        });

        join.setPrefSize(130,55);
        join.setMinWidth(130);

        join.setOnAction(value -> {
            sound.play("buttonsRelax");
            Menu.setScene("join");
        });

        joinPublic.setPrefSize(130,55);
        joinPublic.setMinWidth(130);


        joinPublic.setOnAction(value ->{
            sound.play("buttonsRelax");
            Menu.setScene("wait");
        });

        training.setPrefSize(130,55);
        training.setMinWidth(130);
        
        training.setOnAction(value ->{
            sound.play("buttonsRelax");
            Menu.getModel().setBot(0);
            Menu.setView(1, true);
        });

        stopSound.setPrefSize(50,55);
        stopSound.setMinWidth(50);
        stopSound.setGraphic(new ImageView(unmute));
        
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
