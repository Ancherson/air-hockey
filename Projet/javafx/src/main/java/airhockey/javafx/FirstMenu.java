package airhockey.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class FirstMenu extends BorderPane {

    public FirstMenu(MenuClient Menu){
        Button create = new Button("Create room");
        Button join = new Button("join a room");
        Button test = new Button("ENTRAINEMENT");

        HBox BoxB1 = new HBox(create,join);
        BoxB1.setAlignment(Pos.CENTER);

        create.setPrefSize(100,60);
        create.setMinWidth(100);
        create.setOnAction(event -> {
            Menu.setScene(4);
        });


        join.setPrefSize(100,60);
        join.setMinWidth(100);
        join.setOnAction(value -> {
            Menu.setScene(3);
        });

        test.setPrefSize(130,55);
        test.setMinWidth(130);
        test.setOnAction(value ->{
            Menu.setScene(4);
        });
        this.setAlignment(BoxB1, Pos.CENTER);
        this.setCenter(BoxB1);

        this.setAlignment(test,Pos.BASELINE_RIGHT);
        this.setBottom(test);

        this.setPadding(new Insets(30,50,50,50));



    }

}
