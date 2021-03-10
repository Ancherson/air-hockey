package airhockey.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class CreateMenu extends BorderPane {

    public CreateMenu(MenuClient menu) {


        Label creation = new Label("creation de la room");

        Button back = new Button("back");


        creation.setPrefHeight(100);



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


}
