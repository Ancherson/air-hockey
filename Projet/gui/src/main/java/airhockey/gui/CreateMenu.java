package airhockey.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.BLACK;


public class CreateMenu extends BorderPane {
    private Label creation;
    private String idRoom;

    public CreateMenu(MenuClient menu) {

        this.setWidth(800);
        this.setHeight(500);
        this.setStyle("-fx-background-color: #282828;");

        creation = new Label("CREATION OF THE ROOM");
        Button back = new Button("Back");

        Clipboard clipboard = Clipboard.getSystemClipboard();

        ClipboardContent content = new ClipboardContent();

        creation.setPrefHeight(100);
        creation.setStyle("-fx-font : 28 Ubuntu;");
        creation.setTextFill(WHITE);

        creation.setOnMouseClicked((event) -> {
            content.putString(idRoom);
            clipboard.setContent(content);
        } );

        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);
        back.setStyle("-fx-background-color: #565656;");
        back.setTextFill(WHITE);

        back.setOnMouseEntered((action)->{
            back.setStyle("-fx-background-color: #FFFFFF;");
            back.setTextFill(BLACK);
        });

        back.setOnMouseExited((action)->{
            back.setStyle("-fx-background-color: #565656;");
            back.setTextFill(WHITE);
        });

        back.setOnAction(value -> {
            menu.setScene("first");
            menu.closeClient();
        });

        this.setCenter(creation);
        this.setAlignment(creation, Pos.CENTER);


        this.setBottom(back);
        this.setAlignment(back,Pos.BASELINE_RIGHT);

        this.setPadding(new Insets(30,50,50,50));

    }

    public void setID(String id){
        creation.setText(id);
        idRoom = id;
    }


}
