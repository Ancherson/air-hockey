package airhockey.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class FirstMenu extends BorderPane {

    public FirstMenu(MenuClient Menu){
        this.setWidth(800);
        this.setHeight(500);

        Button create = new Button("Create room");
        Button join = new Button("Join a room");
        Button joinPublic = new Button("Join Public Room");
        Button test = new Button("ENTRAINEMENT");

        HBox BoxB1 = new HBox(create,join);
        VBox bigBox = new VBox(BoxB1,joinPublic);
        BoxB1.setAlignment(Pos.CENTER);
        bigBox.setAlignment(Pos.CENTER);


        create.setPrefSize(100,60);
        create.setMinWidth(100);
        create.setOnAction(event -> {
            Menu.setScene(2);

        });


        join.setPrefSize(100,60);
        join.setMinWidth(100);
        join.setOnAction(value -> {
            Menu.setScene(3);
        });

        joinPublic.setPrefSize(150,60);
        joinPublic.setMinWidth(150);
        joinPublic.setOnAction(value ->{
            Menu.joinPublicRoom();
        });

        test.setPrefSize(130,55);
        test.setMinWidth(130);
        test.setOnAction(value ->{
            Menu.setView(1);
        });
        this.setAlignment(bigBox, Pos.CENTER);
        this.setCenter(bigBox);

        this.setAlignment(test,Pos.BASELINE_RIGHT);
        this.setBottom(test);

        this.setPadding(new Insets(30,50,50,50));



    }

}
