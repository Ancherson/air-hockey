package airhockey.javafx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class JoinMenu extends StackPane {


    public JoinMenu(MenuClient menu){

        Label joinT = new Label("rentrer l'id");

        TextField id = new TextField();

        Button back = new Button("back");

        VBox box = new VBox(joinT,id);

        joinT.setMinHeight(100);
        joinT.setMaxHeight(100);


        id.setMaxWidth(300);
        id.setPromptText("id");
        id.setOnMousePressed( event -> {
            id.selectAll();
        });
        id.setOnAction(event -> {
            System.out.println(id.getText());
        });

        back.setMaxSize(100,50);
        back.setPrefSize(60,50);

        back.setOnAction(value -> {
            menu.setScene(1);
        });

        this.setPadding(new Insets(30,50,50,50));
        
        this.setAlignment(box, Pos.CENTER_LEFT);
        this.getChildren().add(box);

        this.setAlignment(back,Pos.BOTTOM_RIGHT);
        this.getChildren().add(back);
    }

}
