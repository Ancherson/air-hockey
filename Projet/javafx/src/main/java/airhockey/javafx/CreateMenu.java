package airhockey.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;



public class CreateMenu extends BorderPane {
    private Label creation;
    private String idRoom;

    public CreateMenu(MenuClient menu) {

        creation = new Label("creation de la room");
        Button back = new Button("back");

        Clipboard clipboard = Clipboard.getSystemClipboard();

        ClipboardContent content = new ClipboardContent();

        creation.setPrefHeight(100);
        creation.setOnMouseClicked((event) -> {
            content.putString(idRoom);
            clipboard.setContent(content);
        } );



        back.setMaxSize(100, 60);
        back.setPrefSize(60, 50);

        back.setOnAction(value -> {
            menu.setScene(1);
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
