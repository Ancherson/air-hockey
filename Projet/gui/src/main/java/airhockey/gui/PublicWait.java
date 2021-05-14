package airhockey.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.WHITE;

/**
 * This class represents the page shown when someones wants to join a public room
 */
public class PublicWait extends BorderPane {

    private Label info;

    /**
     * Constructor of PublicWait
     * @param menu is the main Pane of the gui that allows to navigate
     *             to different pages
     */
    public PublicWait(MenuClient menu){
        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        info = new Label("Connexion to the server");
        info.setPrefHeight(100);
        info.setStyle("-fx-font : 28 Ubuntu;");
        info.setTextFill(WHITE);        this.setCenter(info);

        ClickButton back = new ClickButton("Back");
        this.setAlignment(back,Pos.BASELINE_RIGHT);
        this.setBottom(back);



        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);

        back.setOnAction(value -> {
            menu.setScene("first");
            menu.closeClient();
        });

        this.setPadding(new Insets(30,50,50,50));

    }

    /**
     * Function that sets a message signaling that the player is inside a room
     * and is waiting for an other player
     */
    public void connected(){
        info.setText("Connected to the server, waiting for an other player");
    }

}
