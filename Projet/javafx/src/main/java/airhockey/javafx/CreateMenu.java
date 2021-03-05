package airhockey.javafx;

import airhockey.model.Model;
import airhockey.network.Client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.SocketException;


public class CreateMenu extends BorderPane {

    public CreateMenu(MenuClient menu, Model model) {


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

        System.out.println("EN ATTENTE DU SERVEUR");
        menu.createRoom();

        System.out.println("Ca commence !!!");

    }


}
